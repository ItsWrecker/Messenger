package com.qxlabai.data.datastore.serializer

import androidx.datastore.core.Serializer
import com.qxlabai.data.datastore.entiry.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object PreferencesSerializer : Serializer<Preferences> {

    override val defaultValue: Preferences
        get() = Preferences.getDefaultInstances()

    override suspend fun readFrom(input: InputStream): Preferences {
        return try {
            Json.decodeFromString(
                deserializer = Preferences.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: Preferences, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = Preferences.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}