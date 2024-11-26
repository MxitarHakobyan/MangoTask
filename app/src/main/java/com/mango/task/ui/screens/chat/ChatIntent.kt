package com.mango.task.ui.screens.chat

sealed class ChatIntent {
    data class SendMessage(val message: String) : ChatIntent()
    data object LoadMessages : ChatIntent()
}
