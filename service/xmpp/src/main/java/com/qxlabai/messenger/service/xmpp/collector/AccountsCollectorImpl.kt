package com.qxlabai.messenger.service.xmpp.collector

import android.util.Log
import com.qxlabai.messenger.core.model.data.Account
import com.qxlabai.messenger.core.model.data.AccountStatus.LoggingIn
import com.qxlabai.messenger.core.model.data.AccountStatus.Registering
import com.qxlabai.messenger.core.model.data.AccountStatus.ShouldLogin
import com.qxlabai.messenger.core.model.data.AccountStatus.ShouldRegister
import com.qxlabai.messenger.core.data.repository.PreferencesRepository
import javax.inject.Inject

class AccountsCollectorImpl @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : AccountsCollector {

    override suspend fun collectAccounts(
        onNewLogin: suspend (Account) -> Unit,
        onNewRegister: suspend (Account) -> Unit
    ) {
        preferencesRepository.getAccount().collect { account ->
            Log.d("Collector", "Collecting account: $account")
            if (account.status == ShouldLogin) {
                preferencesRepository.updateAccount(account.copy(status = LoggingIn))
                onNewLogin(account)
            }
            if (account.status == ShouldRegister) {
                preferencesRepository.updateAccount(account.copy(status = Registering))
                onNewRegister(account)
            }
        }
    }
}
