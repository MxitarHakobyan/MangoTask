package com.mango.task.ui.screens.chatList

import com.mango.task.ui.screens.chat.ChatMessage

data class Chat(
    val id: String,
    val userName: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val messages: List<ChatMessage>,
)