package com.qxlabai.messenger.core.data.repository

import com.qxlabai.messenger.core.model.data.Account
import com.qxlabai.messenger.core.model.data.ConnectionStatus
import com.qxlabai.messenger.core.model.data.ThemeConfig
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    fun getConnectionStatus(): Flow<ConnectionStatus>

    suspend fun updateConnectionStatus(connectionStatus: ConnectionStatus)

    fun getAccount(): Flow<Account>

    suspend fun updateAccount(account: Account)

    fun getThemeConfig(): Flow<ThemeConfig>

    suspend fun updateThemeConfig(themeConfig: ThemeConfig)
}
