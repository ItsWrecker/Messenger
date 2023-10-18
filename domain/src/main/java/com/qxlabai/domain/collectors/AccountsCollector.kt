package com.qxlabai.domain.collectors

import com.qxlabai.domain.entity.Account

interface AccountsCollector {
    /**
     * Collects the changes to accounts stream originated from database
     * */
    suspend fun collectAccounts(
        onNewLogin: suspend (Account) -> Unit,
        onNewRegister: suspend (Account) -> Unit
    )
}
