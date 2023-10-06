package com.qxlabai.data.datastore.serializer

import androidx.datastore.core.Serializer
import com.qxlabai.data.datastore.entiry.AppLock
import com.qxlabai.domain.interactors.xmpp.entity.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object CredentialSerializer : Serializer<Credentials> {

    override val defaultValue: Credentials
        get() = Credentials("", "")


    override suspend fun readFrom(input: InputStream): Credentials {
        return try {
            Json.decodeFromString(
                deserializer = Credentials.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: Credentials, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = Credentials.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}