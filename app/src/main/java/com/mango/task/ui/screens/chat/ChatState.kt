package com.mango.task.ui.screens.chat

data class ChatState(
    val chatName: String = "…",
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
)