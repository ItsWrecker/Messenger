package com.qxlabai.messenger.core.datastore.erase

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EraseDataSource @Inject constructor(
    private val dataStore: DataStore<ErasePreferences>
) {

    suspend fun doesAttemptLeft(): Pair<Boolean, Int> {
        val attempts = dataStore.data.first().incorrectPassCount
        return Pair(first = attempts < 3, attempts)
    }

    suspend fun attempts() = dataStore.data.first().incorrectPassCount

    suspend fun updateAttempt(isCorrect: Boolean) {
        val count = dataStore.data.first().incorrectPassCount
        dataStore.updateData {
            if (isCorrect) {
                it.copy(incorrectPassCount = 0)
            } else {

                it.copy(incorrectPassCount = count + 1)
            }
        }
    }

    fun attemptsStreams(): Flow<ErasePreferences> = dataStore.data.map {
        ErasePreferences(it.incorrectPassCount)
    }.distinctUntilChanged()
}