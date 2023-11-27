package com.qxlabai.messenger.core.model.data

import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant

data class Contact(
    val jid: String,
    val presence: Presence,
    val lastTime: Instant,
    /**
     * To flag the contact to be added to roster entries
     */
    val shouldAddToRoster: Boolean,
    val subscribed: Subscribed
) {

    enum class Subscribed {
        APPROVED,
        PENDING
    }

    val firstLetter: String
        get() = jid.take(1).uppercase()

    companion object {
        fun create(jid: String): Contact =
            Contact(
                jid = "$jid@wrecker.in",
                presence = Presence(),
                lastTime = System.now(),
                shouldAddToRoster = true,
                Subscribed.PENDING
            )

        fun createWithSubscription(jid: String): Contact {
            return Contact(
                jid = jid,
                presence = Presence(),
                lastTime = System.now(),
                shouldAddToRoster = true,
                Subscribed.APPROVED
            )
        }
    }
}
