package com.mango.task.ui.screens.profile

sealed class ProfileEvent {
    data object ShowSaveSuccess : ProfileEvent()
    data object ShowError : ProfileEvent()
}