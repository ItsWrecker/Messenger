package com.qxlabai.presentation.connection

import android.util.Log
import com.qxlabai.domain.collectors.ChatStateCollector
import com.qxlabai.domain.collectors.MessagesCollector
import com.qxlabai.domain.collectors.MessagesRepository
import com.qxlabai.domain.entity.Message
import com.qxlabai.domain.entity.MessageStatus
import com.qxlabai.domain.entity.SendingChatState
import com.qxlabai.domain.repositories.ConversationsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener
import org.jivesoftware.smack.packet.MessageBuilder
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smackx.chatstates.ChatStateListener
import org.jivesoftware.smackx.chatstates.ChatStateManager
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener
import org.jxmpp.jid.impl.JidCreate
import javax.inject.Inject
import com.qxlabai.presentation.connection.asSmackEnum
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smackx.chatstates.ChatState
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.Jid

class MessageManagerImpl @Inject constructor(
    private val messagesCollector: MessagesCollector,
    private val chatStateCollector: ChatStateCollector,
    private val messagesRepository: MessagesRepository,
    private val conversationsRepository: ConversationsRepository
) : MessageManager {

    companion object {
        private val TAG = MessageManager::class.java.simpleName
    }
    private val scope: CoroutineScope by lazy {
        return@lazy CoroutineScope(SupervisorJob(parent = null))
    }

    private lateinit var chatManager: ChatManager
    private lateinit var chatStateManager: ChatStateManager
    private lateinit var deliveryReceiptManager: DeliveryReceiptManager

    private var incomingChatMessageListener: IncomingChatMessageListener? = null
    private var outgoingChatMessageListener: OutgoingChatMessageListener? = null
    private var chatStateListener: ChatStateListener? = null
    private var receiptReceivedListener: ReceiptReceivedListener? = null

    override suspend fun initialize(connection: XMPPTCPConnection) {
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
        deliveryReceiptManager.autoReceiptMode = DeliveryReceiptManager.AutoReceiptMode.always
        deliveryReceiptManager.autoAddDeliveryReceiptRequests()
        receiptReceivedListener = ReceiptReceivedListener(::handleReceivedReceipt)
        deliveryReceiptManager.addReceiptReceivedListener(receiptReceivedListener)
    }

    private fun handleIncomingMessage(
        from: EntityBareJid,
        message: org.jivesoftware.smack.packet.Message,
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
        state: ChatState,
        message: org.jivesoftware.smack.packet.Message
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
                messagesRepository.updateMessage(it.withStatus(status = MessageStatus.SentDelivered))
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

}