package com.qxlabai.domain.repositories

import com.qxlabai.domain.events.Events
import com.qxlabai.domain.interactors.xmpp.entity.Credentials
import kotlinx.coroutines.flow.Flow

interface CredentialsRepository {

    suspend fun updateCredentials(
        credentials: Credentials
    ): Flow<Events<String>>

    suspend fun fetchCredentials(): Flow<Events<Credentials>>

    suspend fun isAppAuthenticated(): Flow<Events<Boolean>>
}