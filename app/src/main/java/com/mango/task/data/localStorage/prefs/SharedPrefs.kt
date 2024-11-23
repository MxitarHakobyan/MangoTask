package com.mango.task.data.localStorage.prefs

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefs @Inject constructor(context: Context) {

    companion object {
        private const val PREFS_NAME = "mango_prefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}