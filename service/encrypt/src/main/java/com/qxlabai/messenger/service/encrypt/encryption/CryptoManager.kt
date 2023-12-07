//package com.qxlabai.messenger.service.encrypt.encryption
//
//import android.security.keystore.KeyGenParameterSpec
//import android.security.keystore.KeyProperties
//import java.io.InputStream
//import java.io.OutputStream
//import java.security.KeyStore
//import javax.crypto.Cipher
//import javax.crypto.KeyGenerator
//import javax.crypto.SecretKey
//import javax.crypto.spec.IvParameterSpec
//
//interface CryptoManager {
//
//
//    fun encrypt(bytes: ByteArray, outputStream: OutputStream): ByteArray
//
//    fun decrypt(inputStream: InputStream): ByteArray
//
//}