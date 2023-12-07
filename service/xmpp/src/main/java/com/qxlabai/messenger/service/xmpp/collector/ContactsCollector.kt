package com.qxlabai.messenger.service.xmpp.collector

import com.qxlabai.messenger.core.model.data.Contact

interface ContactsCollector {
    /**
     * Collects the changes to contacts stream which should be added
     * to roster entries, originated from database
     * */
    suspend fun collectShouldAddToRosterContacts(
        addToRoster: suspend (List<Contact>) -> Unit
    )
}
