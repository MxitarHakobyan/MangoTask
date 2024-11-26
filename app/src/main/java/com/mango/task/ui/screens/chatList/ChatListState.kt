package com.mango.task.ui.screens.chatList

data class ChatListState(
    val isLoading: Boolean = false,
    val chats: List<Chat> = emptyList(),
    val errorMessage: String? = null
)