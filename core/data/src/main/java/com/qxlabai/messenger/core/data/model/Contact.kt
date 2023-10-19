package com.qxlabai.messenger.core.data.model

import com.qxlabai.messenger.core.model.data.Contact
import com.qxlabai.messenger.core.database.model.ContactEntity

fun Contact.asEntity() = ContactEntity(
    jid = jid,
    presenceType = presence.type,
    presenceMode = presence.mode,
    presenceStatus = presence.status,
    presencePriority = presence.priority,
    lastTime = lastTime,
    shouldAddToRoster = shouldAddToRoster
)
