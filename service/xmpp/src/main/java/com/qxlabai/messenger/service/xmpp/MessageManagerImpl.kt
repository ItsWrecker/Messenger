package com.qxlabai.messenger.service.xmpp

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.messaging.ktx.remoteMessage
import com.qxlabai.messenger.core.data.repository.ContactsRepository
import com.qxlabai.messenger.core.data.repository.ConversationsRepository
import com.qxlabai.messenger.core.data.repository.MessagesRepository
import com.qxlabai.messenger.core.model.data.Conversation
import com.qxlabai.messenger.core.model.data.Message
import com.qxlabai.messenger.core.model.data.MessageStatus.SentDelivered
import com.qxlabai.messenger.core.model.data.Presence
import com.qxlabai.messenger.core.model.data.SendingChatState
import com.qxlabai.messenger.service.xmpp.collector.ChatStateCollector
import com.qxlabai.messenger.service.xmpp.collector.MessagesCollector
import com.qxlabai.messenger.service.xmpp.model.asExternalEnum
import com.qxlabai.messenger.service.xmpp.model.asSmackEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener
import org.jivesoftware.smack.packet.MessageBuilder
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smack.packet.StanzaBuilder
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smackx.chatstates.ChatStateListener
import org.jivesoftware.smackx.chatstates.ChatStateManager
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jivesoftware.smackx.omemo.OmemoMessage
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager.AutoReceiptMode.always
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener
import org.jivesoftware.smackx.vcardtemp.VCardManager
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.Jid
import org.jxmpp.jid.impl.JidCreate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject
import kotlin.random.Random
import org.jivesoftware.smack.packet.Message as SmackMessage
import org.jivesoftware.smackx.chatstates.ChatState as SmackChatState

private const val TAG = "MessagesManagerImpl"

class MessageManagerImpl @Inject constructor(
    private val messagesCollector: MessagesCollector,
    private val chatStateCollector: ChatStateCollector,
    private val messagesRepository: MessagesRepository,
    private val conversationsRepository: ConversationsRepository,
    private val contactsRepository: ContactsRepository,
    private val fcmApi: Api
) : MessageManager {

    private val scope = CoroutineScope(SupervisorJob())

    private lateinit var chatManager: ChatManager
    private lateinit var chatStateManager: ChatStateManager
    private lateinit var deliveryReceiptManager: DeliveryReceiptManager

    private var incomingChatMessageListener: IncomingChatMessageListener? = null
    private var outgoingChatMessageListener: OutgoingChatMessageListener? = null
    private var chatStateListener: ChatStateListener? = null
    private var receiptReceivedListener: ReceiptReceivedListener? = null

    private lateinit var omemoManager: OmemoManager
    private lateinit var connection: XMPPTCPConnection

    override suspend fun initialize(connection: XMPPTCPConnection, omemoManager: OmemoManager) {
        chatManager = ChatManager.getInstanceFor(connection)
        chatStateManager = ChatStateManager.getInstance(connection)
        deliveryReceiptManager = DeliveryReceiptManager.getInstanceFor(connection)

        try {
            this.omemoManager = omemoManager
            this.connection = connection
            omemoManager.purgeDeviceList()
        } catch (exception: Exception) {
            Log.e(TAG, exception.message, exception)
        }

        scope.launch {
            messagesCollector.collectShouldSendMessages(sendMessages = ::sendMessages)
        }

        scope.launch {
            chatStateCollector.collectChatState(onChatStateChanged = ::sendChatState)
        }

        observeOutgoingMessages()
        observeChatState()
        observeDeliveryReceipt()
    }

    // blocking
    private fun sendMessages(messages: List<Message>) {
        messages.forEach { message ->
            val jid = JidCreate.bareFrom(message.peerJid)
            try {
                omemoManager.requestDeviceListUpdateFor(jid)
                if (omemoManager.getDevicesOf(jid)?.isEmpty() == true) {
                    val roster = Roster.getInstanceFor(connection)
                    roster.getPresence(jid)
                }



                omemoManager.getActiveFingerprints(jid)?.let { map ->
                    Log.e(TAG, map.toString())
                    map.entries.forEach {
                        omemoManager.trustOmemoIdentity(it.key, it.value)
                    }
                }
                val encryptedMessage = omemoManager.encrypt(jid, message.body)
                val messageStanza = encryptedMessage.buildMessage(
                    StanzaBuilder.buildMessage(message.stanzaId)
                        .addExtension(encryptedMessage.element),
                    jid
                )
                connection.sendStanza(messageStanza)

                try {
                    val vcardManager = VCardManager.getInstanceFor(connection)
                    val vCard = vcardManager.loadVCard(jid.asBareJid().asEntityBareJidOrThrow())
                    if (vCard.emailHome.isNullOrEmpty().not()) {

                        scope.launch(Dispatchers.IO) {
                            kotlin.runCatching {
                                fcmApi.sendFcmMessage(
                                    FcmMessage(
                                        to = vCard.emailHome,
                                        FcmNotification(
                                            "message",
                                            "from: ${connection.user.localpartOrNull}"
                                        )
                                    )
                                )
                            }
                        }
                    }
                } catch (exception: Exception) {
                    Log.e(TAG, exception.message, exception)
                }

            } catch (exception: Exception) {
                Log.e(TAG, exception.message, exception)
            }
        }
    }

    // blocking
    private fun sendChatState(sendingChatState: SendingChatState) {
        val chat = chatManager.chatWith(JidCreate.entityBareFrom(sendingChatState.peerJid))
        chatStateManager.setCurrentState(sendingChatState.chatState.asSmackEnum(), chat)
    }

//    private fun observeIncomingMessages() {
//        incomingChatMessageListener = IncomingChatMessageListener(::handleIncomingMessage)
//        chatManager.addIncomingListener(incomingChatMessageListener)
//    }

    private fun observeOutgoingMessages() {
        outgoingChatMessageListener = OutgoingChatMessageListener(::handleOutgoingMessage)
        chatManager.addOutgoingListener(outgoingChatMessageListener)
    }

    private fun observeChatState() {
        chatStateListener = ChatStateListener(::handleChatState)
        chatStateManager.addChatStateListener(chatStateListener)
    }

    private fun observeDeliveryReceipt() {
        deliveryReceiptManager.autoReceiptMode = always
        deliveryReceiptManager.autoAddDeliveryReceiptRequests()
        receiptReceivedListener = ReceiptReceivedListener(::handleReceivedReceipt)
        deliveryReceiptManager.addReceiptReceivedListener(receiptReceivedListener)
    }

//    private fun handleIncomingMessage(
//        from: EntityBareJid,
//        message: SmackMessage,
//        chat: Chat
//    ) {
//        Log.d(TAG, "IncomingListener - from: $from, message: $message, chat: $chat")
//
//        scope.launch {
//            messagesRepository.handleIncomingMessage(
//                message = message.asExternalModel(),
//                maybeNewConversation = from.asConversation()
//            )
//        }
//    }

    // TODO: This indicates that Smack have been tried to send the message and
    //  actually this does not mean that server received the message.
    //  Use proper mechanism to determine the message received by server
    private fun handleOutgoingMessage(
        to: EntityBareJid,
        messageBuilder: MessageBuilder,
        chat: Chat
    ) {
        Log.d(TAG, "OutgoingListener - to: $to, messageBuilder: $messageBuilder, chat: $chat")

        scope.launch {
            messagesRepository.handleOutgoingMessage(messageBuilder.stanzaId)
        }
    }

    private fun handleChatState(
        chat: Chat,
        state: SmackChatState,
        message: SmackMessage
    ) {
        Log.d(TAG, "ChatStateListener - state: $state, message: $message, chat: $chat")

        scope.launch {
            val peerJid = chat.xmppAddressOfChatPartner.toString()
            conversationsRepository.updateConversation(
                peerJid = peerJid,
                chatState = state.asExternalEnum()
            )
        }
    }

    private fun handleReceivedReceipt(
        fromJid: Jid,
        toJid: Jid,
        receiptId: String,
        receipt: Stanza
    ) {
        Log.d(
            TAG,
            "addReceiptReceivedListener - " +
                    "fromJid: $fromJid, toJid: $toJid, " +
                    "receiptId: $receiptId, receipt: $receipt"
        )

        scope.launch {
            // TODO: if user has multiple clients this will return null because stanzaId
            //  does not exist for other clients
            val message = messagesRepository.getMessageByStanzaId(receiptId).first()
            message?.let {
                messagesRepository.updateMessage(it.withStatus(status = SentDelivered))
            }
        }
    }

    override fun onCleared() {
        scope.cancel()
        if (this::chatManager.isInitialized) {
            chatManager.removeIncomingListener(incomingChatMessageListener)
            chatManager.removeOutgoingListener(outgoingChatMessageListener)
        }
        if (this::chatStateManager.isInitialized) {
            chatStateManager.removeChatStateListener(chatStateListener)
        }
        if (this::deliveryReceiptManager.isInitialized) {
            deliveryReceiptManager.removeReceiptReceivedListener(receiptReceivedListener)
        }
    }

    override fun handleIncomingMessage(stanza: Stanza?, decryptedMessage: OmemoMessage.Received?) {
        scope.launch {
            try {
                val stanzaId = stanza?.stanzaId
                val message = decryptedMessage?.body
                val peerJid = decryptedMessage?.senderDevice?.jid?.asBareJid()

                if (stanzaId != null && message != null && peerJid != null) {
                    messagesRepository.handleIncomingMessage(
                        message = Message.createReceivedMessage(
                            stanzaId,
                            message,
                            peerJid.toString()
                        ),
                        maybeNewConversation = Conversation(peerJid.toString())
                    )
                }
            } catch (exception: Exception) {
                Log.e(TAG, exception.message, exception)
            }
        }
    }
}
