package com.qxlabai.messenger.core.datastore.lock

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LockPreferenceDataSource @Inject constructor(
    private val lockPreference: DataStore<LockPreferences>
) {


    suspend fun isFirstTimeUser(): Boolean {
        return lockPreference.data.map {
            it.passcode == ""
        }.first()
    }

   suspend fun verifyPasscode(passcode: String): Boolean {
       return lockPreference.data.map {
            passcode == it.passcode
        }.first()
    }

    suspend fun setPasscode(passcode: String){
        lockPreference.updateData {
            it.copy(passcode = passcode)
        }
        lockPreference.data.first().passcode.let {
            Log.e("Passcode", it.toString())
        }
    }
}