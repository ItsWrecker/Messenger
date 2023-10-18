package com.qxlabai.data.datastore.entiry

import com.qxlabai.data.databse.entity.ContactEntity
import com.qxlabai.domain.entity.Contact

fun Contact.asEntity() = ContactEntity(
    jid = jid,
    presenceType = presence.type,
    presenceMode = presence.mode,
    presenceStatus = presence.status,
    presencePriority = presence.priority,
    lastTime = lastTime,
    shouldAddToRoster = shouldAddToRoster
)