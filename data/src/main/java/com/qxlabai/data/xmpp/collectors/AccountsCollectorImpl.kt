package com.qxlabai.data.xmpp.collectors

import android.util.Log
import com.qxlabai.domain.collectors.AccountsCollector
import com.qxlabai.domain.entity.Account
import com.qxlabai.domain.entity.AccountStatus
import com.qxlabai.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class AccountsCollectorImpl @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : AccountsCollector {

    override suspend fun collectAccounts(
        onNewLogin: suspend (Account) -> Unit,
        onNewRegister: suspend (Account) -> Unit
    ) {
        preferencesRepository.getAccount().collectLatest { account ->
            Log.d("Collector", "Collecting account: $account")
            if (account.status == AccountStatus.ShouldLogin) {
                preferencesRepository.updateAccounts(account.copy(status = AccountStatus.LoggingIn))
                onNewLogin(account)
            }
            if (account.status == AccountStatus.ShouldRegister) {
                preferencesRepository.updateAccounts(account.copy(status = AccountStatus.Registering))
                onNewRegister(account)
            }
        }

    }
}