package com.qxlabai.data.datastore.serializer

import androidx.datastore.core.Serializer
import com.qxlabai.data.datastore.entiry.AppLock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AppLockSerializer : Serializer<AppLock> {
    override val defaultValue: AppLock
        get() = AppLock(passcode = "")

    override suspend fun readFrom(input: InputStream): AppLock {
        return try {
            Json.decodeFromString(
                deserializer = AppLock.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: AppLock, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = AppLock.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }

}