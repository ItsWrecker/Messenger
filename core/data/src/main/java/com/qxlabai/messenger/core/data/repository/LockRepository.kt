package com.qxlabai.messenger.core.data.repository

interface LockRepository {

    suspend fun isFirstTimeUser(): Boolean
    suspend fun verifyPasscode(passcode: String): Boolean

    suspend fun setPasscode(passcode: String)

    suspend fun reset()
}