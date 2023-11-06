package com.qxlabai.messenger.core.datastore.lock

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
class LockPreferenceSerializer @Inject constructor(

) : Serializer<LockPreferences> {

    override val defaultValue: LockPreferences = LockPreferences()

    override suspend fun readFrom(input: InputStream): LockPreferences {
        return try {
            ProtoBuf.decodeFromByteArray(LockPreferences.serializer(), input.readBytes())
        } catch (exception: Exception) {
            return defaultValue
        }
    }


    override suspend fun writeTo(t: LockPreferences, output: OutputStream) {
        try {
            val byteArray = ProtoBuf.encodeToByteArray(LockPreferences.serializer(), t)
            withContext(Dispatchers.IO) {
                output.write(byteArray)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}