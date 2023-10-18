package com.qxlabai.data.datastore.repository

import androidx.datastore.core.DataStore
import com.qxlabai.data.datastore.entiry.Preferences
import com.qxlabai.domain.entity.Account
import com.qxlabai.domain.entity.ConnectionStatus
import com.qxlabai.domain.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val store: DataStore<Preferences>,
) : PreferencesRepository {


    override fun getAccount(): Flow<Account> {
        return store.data.map {
            Account(
                jid = it.accountJid,
                localPart = it.accountLocalPart,
                domainPart = it.accountDomainPart,
                password = it.accountPassword,
                status = it.accountStatus
            )
        }.distinctUntilChanged()

    }

    override fun getConnectionStatus(): Flow<ConnectionStatus> {
        return store.data.distinctUntilChanged().map {
            ConnectionStatus(
                availability = it.connectionAvailability,
                authenticated = it.connectionAuthenticated
            )
        }.distinctUntilChanged()
    }

    override suspend fun updateAccounts(account: Account) {
        try {
            store.updateData { preferences ->
                preferences.copy(
                    accountJid = account.jid,
                    accountLocalPart = account.localPart,
                    accountDomainPart = account.domainPart,
                    accountPassword = account.password,
                    accountStatus = account.status
                )
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    override suspend fun updateConnectionStatus(connectionStatus: ConnectionStatus) {
        try {
            store.updateData { preferences: Preferences ->
                preferences.copy(
                    connectionAvailability = connectionStatus.availability,
                    connectionAuthenticated = connectionStatus.authenticated
                )
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}