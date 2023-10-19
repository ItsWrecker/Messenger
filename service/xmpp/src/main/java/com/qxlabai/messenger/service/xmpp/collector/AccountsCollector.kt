package com.qxlabai.messenger.service.xmpp.collector

import com.qxlabai.messenger.core.model.data.Account

interface AccountsCollector {
    /**
     * Collects the changes to accounts stream originated from database
     * */
    suspend fun collectAccounts(
        onNewLogin: suspend (Account) -> Unit,
        onNewRegister: suspend (Account) -> Unit
    )
}
