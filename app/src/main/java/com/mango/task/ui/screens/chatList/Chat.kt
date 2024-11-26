package com.mango.task.ui.screens.chatList

data class Chat(
    val id: Int,
    val userName: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0
)