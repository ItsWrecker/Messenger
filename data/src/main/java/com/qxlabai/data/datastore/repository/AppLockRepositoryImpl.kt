package com.qxlabai.data.datastore.repository

import androidx.datastore.core.DataStore
import com.qxlabai.data.datastore.entiry.AppLock
import com.qxlabai.domain.events.Events
import com.qxlabai.domain.repositories.AppLockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AppLockRepositoryImpl @Inject constructor(
    private val data: DataStore<AppLock>
) : AppLockRepository{

    override suspend fun setPassCode(passCode: String) {
        data.updateData {
            it.copy(passcode = passCode)
        }
    }

    override suspend fun verifyPassCode(passCode: String): Flow<Events<Boolean>> = flow {
        try {
            emit(Events.Loading("Verifying the passcode"))
            val passcode = data.data.first().passcode
            return@flow emit(Events.Success(passCode == passcode))
        } catch (exception: Exception) {
            return@flow emit(Events.Error("Error while verifying the passcode", exception))
        }
    }.flowOn(Dispatchers.IO)
}