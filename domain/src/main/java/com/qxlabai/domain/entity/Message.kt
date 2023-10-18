package com.qxlabai.domain.entity
import java.util.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class Message(
    val id: Long? = null,
    val stanzaId: String,
    val peerJid: String,
    val body: String,
    val sendTime: Instant = Clock.System.now(),
    val status: MessageStatus
) {
    // This message has been sent by current logged-in account
    val isMine: Boolean
        get() = status != MessageStatus.Received && status != MessageStatus.ReceivedDisplayed

    // Change status of the message by creating a new instance
    fun withStatus(status: MessageStatus) = copy(status = status)

    companion object {
        fun createNewMessage(text: String, peerJid: String): Message =
            Message(
                stanzaId = UUID.randomUUID().toString(),
                peerJid = peerJid,
                body = text,
                status = MessageStatus.ShouldSend
            )

        fun createReceivedMessage(stanzaId: String, text: String, peerJid: String): Message =
            Message(
                stanzaId = stanzaId,
                peerJid = peerJid,
                body = text,
                status = MessageStatus.Received
            )
    }
}
