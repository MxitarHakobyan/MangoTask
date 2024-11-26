package com.mango.task.ui.screens.chatList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ChatListState())
    val state: StateFlow<ChatListState> = _state

    init {
        handleIntent(ChatListIntent.LoadChats)
    }

    fun handleIntent(intent: ChatListIntent) {
        when (intent) {
            is ChatListIntent.LoadChats -> loadChats()
            is ChatListIntent.RefreshChats -> refreshChats()
        }
    }

    private fun loadChats() {
        _state.value = ChatListState(isLoading = true)

        viewModelScope.launch {
            delay(1000)
            val dummyChats = createDummyChats()
            _state.value = ChatListState(chats = dummyChats)
        }
    }

    private fun refreshChats() {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            delay(1000)
            val refreshedChats = createDummyChats().map {
                it.copy(lastMessage = "${it.lastMessage} (updated)")
            }
            _state.value = _state.value.copy(chats = refreshedChats, isLoading = false)
        }
    }

    private fun createDummyChats(): List<Chat> {
        return listOf(
            Chat(
                id = 1,
                userName = "Alice",
                lastMessage = "Hey, how are you?",
                time = "10:00 AM",
                unreadCount = 2
            ),
            Chat(id = 2, userName = "Bob", lastMessage = "See you soon!", time = "09:45 AM"),
            Chat(id = 2, userName = "Bob", lastMessage = "See you soon!", time = "09:45 AM"),
            Chat(id = 2, userName = "Bob", lastMessage = "See you soon!", time = "09:45 AM"),
            Chat(id = 2, userName = "Bob", lastMessage = "See you soon!", time = "09:45 AM"),
            Chat(id = 2, userName = "Alan", lastMessage = "Hey!", time = "09:46 AM"),
            Chat(
                id = 3,
                userName = "Charlie",
                lastMessage = "Don't forget the meeting",
                time = "Yesterday"
            ),
            Chat(
                id = 4,
                userName = "Diana",
                lastMessage = "Let's grab lunch",
                time = "Yesterday",
                unreadCount = 1
            ),
            Chat(id = 5, userName = "Eve", lastMessage = "Happy Birthday!", time = "2 days ago")
        )
    }
}