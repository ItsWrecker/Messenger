package com.qxlabai.domain.repositories

import com.qxlabai.domain.events.Events
import kotlinx.coroutines.flow.Flow

interface AppLockRepository {

    suspend fun setPassCode(passCode: String)

    suspend fun verifyPassCode(passCode: String): Flow<Events<Boolean>>
}