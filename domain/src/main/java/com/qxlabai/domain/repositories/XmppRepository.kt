package com.qxlabai.domain.repositories

import com.qxlabai.domain.events.Events
import kotlinx.coroutines.flow.Flow

interface XmppRepository {

    suspend fun connect(): Flow<Events<Boolean>>
    suspend fun disconnect(): Boolean

    suspend fun authenticate(username: String, passcode: String): Flow<Events<String>>

    suspend fun createAccount(username: String, passcode: String): Flow<Events<String>>

    suspend fun getUserId(): Flow<Events<String>>

}