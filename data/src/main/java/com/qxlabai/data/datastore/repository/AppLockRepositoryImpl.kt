package com.qxlabai.data.datastore.repository

import android.content.Context
import androidx.datastore.dataStore
import com.qxlabai.data.datastore.serializer.AppLockSerializer
import com.qxlabai.domain.events.Events
import com.qxlabai.domain.repositories.AppLockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AppLockRepositoryImpl @Inject constructor(
    private val context: Context
) : AppLockRepository {

    private val Context.dataStore by dataStore("messenger.json", AppLockSerializer)

    override suspend fun setPassCode(passCode: String) {
        context.dataStore.updateData {
            it.copy(passcode = passCode)
        }
    }

    override suspend fun verifyPassCode(passCode: String): Flow<Events<Boolean>> = flow {
        try {
            emit(Events.Loading("Verifying the passcode"))
            val passcode = context.dataStore.data.first().passcode
            return@flow emit(Events.Success(passCode == passcode))
        } catch (exception: Exception) {
            return@flow emit(Events.Error("Error while verifying the passcode", exception))
        }
    }.flowOn(Dispatchers.IO)
}