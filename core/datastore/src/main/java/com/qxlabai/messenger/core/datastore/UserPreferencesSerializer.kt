package com.qxlabai.messenger.core.datastore

import androidx.datastore.core.Serializer
import com.qxlabai.messenger.core.datastore.encryption.CryptoManager
import com.qxlabai.messenger.core.datastore.encryption.CryptoManagerImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * An [androidx.datastore.core.Serializer] for the [UserPreferences] proto.
 */
@OptIn(ExperimentalSerializationApi::class)
class UserPreferencesSerializer @Inject constructor() : Serializer<UserPreferences> {

    private val cryptoManager: CryptoManager = CryptoManagerImpl()
    override val defaultValue: UserPreferences
        get() = UserPreferences()


    override suspend fun readFrom(input: InputStream): UserPreferences =
        try {
//            ProtoBuf.decodeFromByteArray(UserPreferences.serializer(), cryptoManager.decrypt(input))
            ProtoBuf.decodeFromByteArray(UserPreferences.serializer(), input.readBytes())
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
//        try {
//            val byteArray = ProtoBuf.encodeToByteArray(UserPreferences.serializer(), t)
//            val encryptedBytes = cryptoManager.encrypt(byteArray, output)
//            withContext(Dispatchers.IO) {
//                output.write(encryptedBytes)
//            }
//        } catch (exception: Exception) {
//            exception.printStackTrace()
//        }
        val byteArray = ProtoBuf.encodeToByteArray(UserPreferences.serializer(), t)
        withContext(Dispatchers.IO) {
            output.write(byteArray)
        }
    }
}
