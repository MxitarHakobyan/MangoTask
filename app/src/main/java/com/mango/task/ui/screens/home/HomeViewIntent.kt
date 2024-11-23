package com.mango.task.ui.screens.home

sealed class HomeViewIntent {
    data object NavigateToLogin : HomeViewIntent()
    data class ShowMessage(val message: String) : HomeViewIntent()
}