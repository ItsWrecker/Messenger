package com.qxlabai.messenger.core.data.repository

import com.qxlabai.messenger.core.model.data.Account
import com.qxlabai.messenger.core.model.data.ConnectionStatus
import com.qxlabai.messenger.core.model.data.ThemeConfig
import com.qxlabai.messenger.core.data.model.asPreferences
import com.qxlabai.messenger.core.datastore.DialoguePreferencesDataSource
import com.qxlabai.messenger.core.datastore.PreferencesAccount
import com.qxlabai.messenger.core.datastore.PreferencesThemeConfig
import com.qxlabai.messenger.core.datastore.asExternalModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesRepositoryImpl @Inject constructor(
    private val preferencesDataSource: DialoguePreferencesDataSource,
) : PreferencesRepository {

    override fun getConnectionStatus(): Flow<ConnectionStatus> =
        preferencesDataSource.getConnectionStatus().map { it.asExternalModel() }

    override suspend fun updateConnectionStatus(connectionStatus: ConnectionStatus) =
        preferencesDataSource.updateConnectionStatus(connectionStatus.asPreferences())

    override fun getAccount(): Flow<Account> =
        preferencesDataSource.getAccount().map(PreferencesAccount::asExternalModel)

    override suspend fun updateAccount(account: Account) =
        preferencesDataSource.updateAccount(account.asPreferences())

    override fun getThemeConfig(): Flow<ThemeConfig> =
        preferencesDataSource.getThemeConfig().map(PreferencesThemeConfig::asExternalModel)

    override suspend fun updateThemeConfig(themeConfig: ThemeConfig) =
        preferencesDataSource.updateThemeConfig(themeConfig.asPreferences())
}
