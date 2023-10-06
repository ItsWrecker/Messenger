package com.qxlabai.data.datastore.repository

import android.content.Context
import androidx.datastore.core.DataStore
import com.qxlabai.data.datastore.serializer.CredentialSerializer
import com.qxlabai.domain.events.Events
import com.qxlabai.domain.interactors.xmpp.entity.Credentials
import com.qxlabai.domain.repositories.CredentialsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class CredentialsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Credentials>
) : CredentialsRepository {

    override suspend fun fetchCredentials(): Flow<Events<Credentials>> = flow {
        emit(Events.Loading("Fetching the user"))
        try {
            val data = dataStore.data.last()
            if (data.passcode.isNotEmpty() && data.username.isNotEmpty()) return@flow emit(
                Events.Success(
                    Credentials(username = data.username, passcode = data.passcode)

                )
            )
            else return@flow emit(Events.Error("Error while fetching the users"))
        } catch (exception: Exception) {
            return@flow emit(Events.Error("Error while fetching the credentials", exception))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun isAppAuthenticated(): Flow<Events<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCredentials(credentials: Credentials): Flow<Events<String>> = flow {
        try {
            emit(Events.Loading("Updating the credentials"))
            dataStore.updateData {
                it.copy(
                    it.username,
                    it.passcode
                )
            }

            return@flow emit(Events.Success("Success"))

        } catch (exception: Exception) {
            return@flow emit(Events.Error("Error while updating the credentials", exception))
        }
    }
}