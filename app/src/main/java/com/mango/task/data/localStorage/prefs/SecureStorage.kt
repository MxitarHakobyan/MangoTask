package com.mango.task.data.localStorage.prefs

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStorage(context: Context) {

    companion object Keys {
        const val KEY_REFRESH_TOKEN = "refresh_token"
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_USER_ID = "user_id"
    }

    private val masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "mango_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun save(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun get(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}