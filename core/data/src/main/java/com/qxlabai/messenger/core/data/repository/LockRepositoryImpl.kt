package com.qxlabai.messenger.core.data.repository

import com.qxlabai.messenger.core.datastore.lock.LockPreferenceDataSource
import javax.inject.Inject

class LockRepositoryImpl @Inject constructor(
    private val lockPreferenceDataSource: LockPreferenceDataSource
) : LockRepository {
    override suspend fun isFirstTimeUser(): Boolean = lockPreferenceDataSource.isFirstTimeUser()
    override suspend fun verifyPasscode(passcode: String): Boolean =
        lockPreferenceDataSource.verifyPasscode(passcode)

    override suspend fun setPasscode(passcode: String) =
        lockPreferenceDataSource.setPasscode(passcode)

    override suspend fun reset() {
        lockPreferenceDataSource.setPasscode("")
    }

}