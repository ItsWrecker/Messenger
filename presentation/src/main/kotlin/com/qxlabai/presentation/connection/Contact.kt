package com.qxlabai.presentation.connection

import com.qxlabai.domain.entity.Contact
import com.qxlabai.domain.entity.Presence
import org.jivesoftware.smack.roster.RosterEntry
import kotlinx.datetime.Clock
fun RosterEntry.asExternalModel() = Contact(
    jid = jid.asBareJid().toString(),
    presence = Presence(),
    lastTime = Clock.System.now(),
    shouldAddToRoster = false
)