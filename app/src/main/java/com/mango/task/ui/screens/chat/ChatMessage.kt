package com.mango.task.ui.screens.chat

data class ChatMessage(
    val id: String,
    val content: String,
    val isSentByUser: Boolean,
    val timestamp: String
)