package com.qxlabai.messenger.core.datastore.erase

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
class EraseSerializer @Inject constructor(

) : Serializer<ErasePreferences> {


    override val defaultValue: ErasePreferences = ErasePreferences()

    override suspend fun readFrom(input: InputStream): ErasePreferences {
        return try {
            ProtoBuf.decodeFromByteArray(ErasePreferences.serializer(), input.readBytes())
        } catch (exception: Exception) {
            return defaultValue
        }

    }

    override suspend fun writeTo(t: ErasePreferences, output: OutputStream) {
        try {
            val byteArray = ProtoBuf.encodeToByteArray(ErasePreferences.serializer(), t)
            withContext(Dispatchers.IO) {
                output.write(byteArray)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}