package com.qxlabai.messenger.core.data.repository

import android.util.Log
import com.qxlabai.messenger.core.datastore.erase.EraseDataSource
import com.qxlabai.messenger.core.datastore.erase.ErasePreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EraseRepositoryImpl @Inject constructor(
    private val eraseDataSource: EraseDataSource
) : EraseRepository {

    override suspend fun attemptsStream(): Flow<ErasePreferences> {
        return eraseDataSource.attemptsStreams()
    }

    override suspend fun updateAttempts(isCorrect: Boolean) {
        eraseDataSource.updateAttempt(isCorrect)
    }

    override suspend fun doesAttemptLeft(): Pair<Boolean, Int> {
        return eraseDataSource.doesAttemptLeft()
    }

    override suspend fun attempts(): Int {
        return eraseDataSource.attempts()
    }

}