package com.qxlabai.domain.repositories

import com.qxlabai.domain.entity.Account
import com.qxlabai.domain.entity.ConnectionStatus
import com.qxlabai.domain.events.Events
import kotlinx.coroutines.flow.Flow


interface PreferencesRepository {

    fun getConnectionStatus(): Flow<ConnectionStatus>

    suspend fun updateConnectionStatus(connectionStatus: ConnectionStatus)

    fun getAccount(): Flow<Account>

    suspend fun updateAccounts(account: Account)


}