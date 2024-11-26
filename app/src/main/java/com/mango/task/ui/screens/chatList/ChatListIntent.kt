package com.mango.task.ui.screens.chatList

sealed class ChatListIntent {
    data object LoadChats : ChatListIntent()
    data object RefreshChats : ChatListIntent()
}