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
) : MessageManager {

    private val scope = CoroutineScope(SupervisorJob())

    private lateinit var chatManager: ChatManager
    private lateinit var chatStateManager: ChatStateManager
    private lateinit var deliveryReceiptManager: DeliveryReceiptManager

    private var incomingChatMessageListener: IncomingChatMessageListener? = null
    private var outgoingChatMessageListener: OutgoingChatMessageListener? = null
    private var chatStateListener: ChatStateListener? = null
    private var receiptReceivedListener: ReceiptReceivedListener? = null

    override suspend fun initialize(connection: XMPPTCPConnection, omemoManager: OmemoManager) {
        chatManager = ChatManager.getInstanceFor(connection)
        chatStateManager = ChatStateManager.getInstance(connection)
        deliveryReceiptManager = DeliveryReceiptManager.getInstanceFor(connection)

        scope.launch {
            messagesCollector.collectShouldSendMessages(sendMessages = ::sendMessages)
        }

        scope.launch {
            chatStateCollector.collectChatState(onChatStateChanged = ::sendChatState)
        }

        observeIncomingMessages()
        observeOutgoingMessages()
        observeChatState()
        observeDeliveryReceipt()
    }

    // blocking
    private fun sendMessages(messages: List<Message>) {
        messages.forEach { message ->
            val chat = chatManager.chatWith(JidCreate.entityBareFrom(message.peerJid))
            val smackMessage = MessageBuilder
                .buildMessage(message.stanzaId)
                .addBody(null, message.body)
                .build()

            chat.send(smackMessage)
        }
    }

    // blocking
    private fun sendChatState(sendingChatState: SendingChatState) {
        val chat = chatManager.chatWith(JidCreate.entityBareFrom(sendingChatState.peerJid))
        chatStateManager.setCurrentState(sendingChatState.chatState.asSmackEnum(), chat)
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

    private fun handleIncomingMessage(
        from: EntityBareJid,
        message: SmackMessage,
        chat: Chat
    ) {
        Log.d(TAG, "IncomingListener - from: $from, message: $message, chat: $chat")

        scope.launch {
            messagesRepository.handleIncomingMessage(
                message = message.asExternalModel(),
                maybeNewConversation = from.asConversation()
            )
        }
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

    }
}
