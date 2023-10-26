package com.qxlabai.messenger.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.qxlabai.messenger.service.encrypt.encryption.CryptoManager
import com.qxlabai.messenger.service.encrypt.encryption.CryptoManagerImpl
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * An [androidx.datastore.core.Serializer] for the [UserPreferences] proto.
 */
class UserPreferencesSerializer @Inject constructor(
//    private val cryptoManager: CryptoManager
) : Serializer<UserPreferences> {

    private val cryptoManager: CryptoManager = CryptoManagerImpl()
    override val defaultValue: UserPreferences
        get() = UserPreferences()

    override suspend fun readFrom(input: InputStream): UserPreferences =
        try {
            val decryptedBytes = cryptoManager.decrypt(input)
            Json.decodeFromString(
                deserializer = UserPreferences.serializer(),
                string = decryptedBytes.decodeToString()
            )
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        cryptoManager.encrypt(
            bytes = Json.encodeToString(UserPreferences.serializer(), t).encodeToByteArray(),
            output
        )
    }
}
