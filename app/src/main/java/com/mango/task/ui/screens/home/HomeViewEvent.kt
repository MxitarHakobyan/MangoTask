package com.mango.task.ui.screens.home

import android.content.Context

sealed class HomeViewEvent {
    data class Logout(val context: Context) : HomeViewEvent()
}