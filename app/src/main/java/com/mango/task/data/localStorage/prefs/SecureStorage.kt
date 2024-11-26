package com.mango.task.data.localStorage.prefs

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.mango.task.BuildConfig.PASSWORD
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class SecureStorage(private val context: Context) {

    companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

    private fun getKey(): SecretKey {
        val key = keyStore.getKey("MySecureKey", PASSWORD.toCharArray()) as SecretKey?
        if (key != null) {
            return key
        } else {
            // If the key doesn't exist, generate it
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").apply {
                    init(
                        KeyGenParameterSpec.Builder(
                            "MySecureKey",
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .build()
                    )
                }
            return keyGenerator.generateKey()
        }
    }

    private fun encryptDataWithKeystore(data: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val key = getKey()
        cipher.init(Cipher.ENCRYPT_MODE, key)

        val iv = cipher.iv
        val encryption = cipher.doFinal(data.toByteArray())

        val encryptedData = Base64.encodeToString(encryption, Base64.DEFAULT)
        val ivBase64 = Base64.encodeToString(iv, Base64.DEFAULT)

        // Return concatenated IV and encrypted data
        return "$ivBase64:$encryptedData"
    }

    private fun decryptDataWithKeystore(encryptedData: String): String {
        val parts = encryptedData.split(":")
        val iv = Base64.decode(parts[0], Base64.DEFAULT)
        val cipherText = Base64.decode(parts[1], Base64.DEFAULT)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, iv)
        val key = getKey()
        cipher.init(Cipher.DECRYPT_MODE, key, spec)

        val decryptedData = cipher.doFinal(cipherText)
        return String(decryptedData)
    }

    fun saveAccessToken(accessToken: String) {
        val encryptedAccessToken = encryptDataWithKeystore(accessToken)
        saveToStorage(KEY_ACCESS_TOKEN, encryptedAccessToken)
    }

    fun saveRefreshToken(refreshToken: String) {
        val encryptedRefreshToken = encryptDataWithKeystore(refreshToken)
        saveToStorage(KEY_REFRESH_TOKEN, encryptedRefreshToken)
    }

    fun getAccessToken(): String? {
        val encryptedAccessToken = getFromStorage(KEY_ACCESS_TOKEN)
        return encryptedAccessToken?.let { decryptDataWithKeystore(it) }
    }

    fun getRefreshToken(): String? {
        val encryptedRefreshToken = getFromStorage(KEY_REFRESH_TOKEN)
        return encryptedRefreshToken?.let { decryptDataWithKeystore(it) }
    }

    private fun saveToStorage(key: String, value: String) {
        val sharedPreferences = context.getSharedPreferences("secure_storage", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun getFromStorage(key: String): String? {
        val sharedPreferences = context.getSharedPreferences("secure_storage", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, null)
    }

    fun clearStorage() {
        val sharedPreferences = context.getSharedPreferences("secure_storage", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}