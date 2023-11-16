package com.qxlabai.messenger.core.data.repository

import com.qxlabai.messenger.core.datastore.erase.ErasePreferences
import kotlinx.coroutines.flow.Flow

interface EraseRepository {

    suspend fun updateAttempts(isCorrect: Boolean)

    suspend fun attemptsStream(): Flow<ErasePreferences>

    suspend fun doesAttemptLeft(): Pair<Boolean, Int>

    suspend fun attempts(): Int
}