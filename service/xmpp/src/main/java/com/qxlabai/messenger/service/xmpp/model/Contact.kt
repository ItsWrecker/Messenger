package com.qxlabai.messenger.service.xmpp.model

import com.qxlabai.messenger.core.model.data.Contact
import com.qxlabai.messenger.core.model.data.Presence
import kotlinx.datetime.Clock
import org.jivesoftware.smack.roster.RosterEntry
import kotlinx.datetime.Clock.System

fun RosterEntry.asExternalModel() = Contact(
    jid = jid.asBareJid().toString(),
    presence = Presence(),
    lastTime = System.now(),
    shouldAddToRoster = false,
    subscribed = Contact.Subscribed.PENDING
)
