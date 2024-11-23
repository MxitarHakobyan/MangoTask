package com.mango.task.ui.navigation

import androidx.lifecycle.ViewModel
import com.mango.task.data.localStorage.prefs.SharedPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    private val sharedPrefs: SharedPrefs
) : ViewModel() {

    fun isLoggedIn(): Boolean {
        return sharedPrefs.isLoggedIn()
    }
}