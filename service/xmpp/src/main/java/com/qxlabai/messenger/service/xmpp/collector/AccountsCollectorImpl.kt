package com.qxlabai.messenger.service.xmpp.collector

import android.content.Context
import android.database.DatabaseUtils
import android.util.Log
import com.qxlabai.messenger.core.common.coroutines.MessengerDispatchers
import com.qxlabai.messenger.core.data.repository.LockRepository
import com.qxlabai.messenger.core.model.data.Account
import com.qxlabai.messenger.core.model.data.AccountStatus.LoggingIn
import com.qxlabai.messenger.core.model.data.AccountStatus.Registering
import com.qxlabai.messenger.core.model.data.AccountStatus.ShouldLogin
import com.qxlabai.messenger.core.model.data.AccountStatus.ShouldRegister
import com.qxlabai.messenger.core.data.repository.PreferencesRepository
import com.qxlabai.messenger.core.model.data.AccountStatus.NotSet
import com.qxlabai.messenger.core.model.data.AccountStatus.Unauthorized
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class AccountsCollectorImpl @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val lockRepository: LockRepository,

    @ApplicationContext private val context: Context
) : AccountsCollector {

    override suspend fun collectAccounts(
        onNewLogin: suspend (Account) -> Unit,
        onNewRegister: suspend (Account) -> Unit,
        onLogout: suspend () -> Unit
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
            if (account.status == Unauthorized) {
                try {
                    preferencesRepository.updateAccount(account.copy("", "", "", "", NotSet))
                    lockRepository.reset()
//                    if (context.cacheDir.exists())
//                        context.cacheDir.deleteRecursively()
//                    if (context.filesDir.exists())
//                        context.filesDir.deleteRecursively()
//                    if (context.dataDir.exists())
//                        context.dataDir.deleteRecursively()

                } catch (exception: Exception) {

                }
               // onLogout()
            }
        }
    }
}
