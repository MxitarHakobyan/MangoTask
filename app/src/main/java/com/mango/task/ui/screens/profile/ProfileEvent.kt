package com.mango.task.ui.screens.profile


sealed class ProfileEvent {
    data object NavigateToLogin : ProfileEvent()
    data class ShowMessage(val message: String) : ProfileEvent()
}