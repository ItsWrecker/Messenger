package com.qxlabai.messenger.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.crypto.tink.Aead
import com.qxlabai.messenger.service.encrypt.encryption.CryptoManager
import com.qxlabai.messenger.service.encrypt.encryption.CryptoManagerImpl
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
class UserPreferencesSerializer @Inject constructor(
//    private val aead: Aead
) : Serializer<UserPreferences> {

    private val cryptoManager: CryptoManager = CryptoManagerImpl()
    override val defaultValue: UserPreferences
        get() = UserPreferences()


    override suspend fun readFrom(input: InputStream): UserPreferences =
        try {
            ProtoBuf.decodeFromByteArray(UserPreferences.serializer(), cryptoManager.decrypt(input))
        } catch (e: SerializationException) {
            throw CorruptionException("Error deserializing proto", e)
        }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        val byteArray = ProtoBuf.encodeToByteArray(UserPreferences.serializer(), t)
        val encryptedBytes = cryptoManager.encrypt(byteArray, output)
        withContext(Dispatchers.IO) {
            output.write(encryptedBytes)
        }
    }
}
