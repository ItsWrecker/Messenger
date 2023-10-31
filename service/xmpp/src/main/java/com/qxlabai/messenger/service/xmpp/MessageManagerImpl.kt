package com.qxlabai.messenger.service.xmpp

import android.content.Context
import android.util.Log
import com.qxlabai.messenger.core.model.data.Message
import com.qxlabai.messenger.core.model.data.MessageStatus.SentDelivered
import com.qxlabai.messenger.core.model.data.SendingChatState
import com.qxlabai.messenger.core.data.repository.ConversationsRepository
import com.qxlabai.messenger.core.data.repository.MessagesRepository
import com.qxlabai.messenger.core.model.data.Conversation
import com.qxlabai.messenger.service.xmpp.collector.ChatStateCollector
import com.qxlabai.messenger.service.xmpp.collector.MessagesCollector
import com.qxlabai.messenger.service.xmpp.model.asConversation
import com.qxlabai.messenger.service.xmpp.model.asExternalEnum
import com.qxlabai.messenger.service.xmpp.model.asExternalModel
import com.qxlabai.messenger.service.xmpp.model.asSmackEnum
import com.qxlabai.messenger.service.xmpp.omemo.EphemeralTrustCallback
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener
import org.jivesoftware.smack.packet.Message as SmackMessage
import org.jivesoftware.smack.packet.MessageBuilder
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smack.packet.StanzaBuilder
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smackx.carbons.packet.CarbonExtension
import org.jivesoftware.smackx.chatstates.ChatState as SmackChatState
import org.jivesoftware.smackx.chatstates.ChatStateListener
import org.jivesoftware.smackx.chatstates.ChatStateManager
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jivesoftware.smackx.omemo.OmemoMessage
import org.jivesoftware.smackx.omemo.OmemoService
import org.jivesoftware.smackx.omemo.listener.OmemoMessageListener
import org.jivesoftware.smackx.omemo.trust.OmemoTrustCallback
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager.AutoReceiptMode.always
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.Jid
import org.jxmpp.jid.impl.JidCreate

private const val TAG = "MessagesManagerImpl"

class MessageManagerImpl @Inject constructor(
    private val messagesCollector: MessagesCollector,
    private val chatStateCollector: ChatStateCollector,
    private val messagesRepository: MessagesRepository,
    private val conversationsRepository: ConversationsRepository,
    @ApplicationContext private val context: Context
) : MessageManager, OmemoMessageListener, OmemoManager.InitializationFinishedCallback {


    private val scope = CoroutineScope(SupervisorJob())

    private val trustCallback: OmemoTrustCallback by lazy {
        return@lazy EphemeralTrustCallback()
    }

    private lateinit var chatManager: ChatManager
    private lateinit var chatStateManager: ChatStateManager
    private lateinit var deliveryReceiptManager: DeliveryReceiptManager


    private lateinit var omemoManager: OmemoManager

    private var incomingChatMessageListener: IncomingChatMessageListener? = null
    private var outgoingChatMessageListener: OutgoingChatMessageListener? = null
    private var chatStateListener: ChatStateListener? = null
    private var receiptReceivedListener: ReceiptReceivedListener? = null
    private lateinit var connection: XMPPTCPConnection

    override suspend fun initialize(connection: XMPPTCPConnection, omemoManager: OmemoManager) {

        try {
            this.connection = connection
            this.omemoManager = omemoManager
            try {
                omemoManager.requestDeviceListUpdateFor(JidCreate.bareFrom(omemoManager.ownJid))

                val deviceList = OmemoService.getInstance().omemoStoreBackend.loadCachedDeviceList(
                    omemoManager.ownDevice,
                    omemoManager.ownJid
                )
                Log.e(TAG, "$deviceList")

//                val pubSubManager = PubSubManager.getInstanceFor(connection, omemoManager.ownJid)
//
//                deviceList.allDevices.forEach {
//                    try {
//                        pubSubManager.getLeafNode(OmemoConstants.PEP_NODE_BUNDLE_FROM_DEVICE_ID(it))
//                            .deleteAllItems()
//                    } catch (exception: Exception) {
//                        exception.printStackTrace()
//                    }
//                    try {
//                        pubSubManager.deleteNode(OmemoConstants.PEP_NODE_BUNDLE_FROM_DEVICE_ID(it))
//                    } catch (exception: Exception) {
//                        exception.printStackTrace()
//                    }
//                }

            } catch (exception: Exception) {

            }

        } catch (exception: Exception) {
            Log.e(TAG, exception.message, exception)
        }

        chatManager = ChatManager.getInstanceFor(connection)
        chatStateManager = ChatStateManager.getInstance(connection)
        deliveryReceiptManager = DeliveryReceiptManager.getInstanceFor(connection)

        scope.launch {
            messagesCollector.collectShouldSendMessages(sendMessages = ::sendMessages)
        }

        scope.launch {
            chatStateCollector.collectChatState(onChatStateChanged = ::sendChatState)
        }


//        observeIncomingMessages()
        observeOutgoingMessages()
        observeChatState()
        observeDeliveryReceipt()
    }


    // blocking
    private fun sendMessages(messages: List<Message>) {
        messages.forEach { message ->

            val contactJid = JidCreate.bareFrom(message.peerJid)

            try {
                val doesSupportOMEMO = omemoManager.contactSupportsOmemo(contactJid)
                println(doesSupportOMEMO)
            } catch (exception: Exception) {
                Log.e(TAG, exception.message, exception)
            }

            val encryptedMessage =
                omemoManager.encrypt(JidCreate.bareFrom(message.peerJid), message.body)
            val msg = encryptedMessage.buildMessage(
                StanzaBuilder.buildMessage(message.stanzaId)
                    .addExtension(encryptedMessage.element),
                JidCreate.bareFrom(message.peerJid)
            )
            connection.sendStanza(msg)
        }
    }

    // blocking
    private fun sendChatState(sendingChatState: SendingChatState) {
        val chat = chatManager.chatWith(JidCreate.entityBareFrom(sendingChatState.peerJid))
        chatStateManager.setCurrentState(sendingChatState.chatState.asSmackEnum(), chat)
    }


    override fun onOmemoCarbonCopyReceived(
        direction: CarbonExtension.Direction?,
        carbonCopy: org.jivesoftware.smack.packet.Message?,
        wrappingMessage: org.jivesoftware.smack.packet.Message?,
        decryptedCarbonCopy: OmemoMessage.Received?
    ) {
        Log.e(TAG, "Message")
    }

    override fun onOmemoMessageReceived(stanza: Stanza?, decryptedMessage: OmemoMessage.Received?) {

        Log.e(TAG, "$decryptedMessage")
    }

    private fun observeIncomingMessages() {
        incomingChatMessageListener = IncomingChatMessageListener(::handleIncomingMessage)
        chatManager.addIncomingListener(incomingChatMessageListener)
    }

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


    override fun handleIncomingMessage(stanza: Stanza?, decryptedMessage: OmemoMessage.Received?) {
        try {

            scope.launch {

                val stanzaId = stanza?.stanzaId
                val message = decryptedMessage?.body
                val peerJid = decryptedMessage?.senderDevice?.jid?.asBareJid()

                if (stanzaId !=null && message !=null && peerJid !=null){
                    messagesRepository.handleIncomingMessage(
                        message = Message.createReceivedMessage(stanzaId, message, peerJid.toString()),
                        maybeNewConversation = Conversation(peerJid.toString())
                    )
                }

            }
        }catch (exception: Exception){
            Log.e(TAG, exception.message, exception)
        }
    }

    private fun handleIncomingMessage(
        from: EntityBareJid,
        message: SmackMessage,
        chat: Chat
    ) {

//        Log.d(TAG, "IncomingListener - from: $from, message: $message, chat: $chat")
//        scope.launch {
//            messagesRepository.handleIncomingMessage(
//                message = message.asExternalModel(),
//                maybeNewConversation = from.asConversation()
//            )
//        }
    }

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

    override fun initializationFinished(manager: OmemoManager?) {
        Log.i(TAG, manager?.deviceId.toString())
    }

    override fun initializationFailed(cause: java.lang.Exception?) {
        Log.e(TAG, cause?.message, cause)
    }
}
