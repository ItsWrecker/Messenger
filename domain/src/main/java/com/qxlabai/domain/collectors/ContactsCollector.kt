package com.qxlabai.domain.collectors

import com.qxlabai.domain.entity.Contact

interface ContactsCollector {
    /**
     * Collects the changes to contacts stream which should be added
     * to roster entries, originated from database
     * */
    suspend fun collectShouldAddToRosterContacts(
        addToRoster: suspend (List<Contact>) -> Unit
    )
}
