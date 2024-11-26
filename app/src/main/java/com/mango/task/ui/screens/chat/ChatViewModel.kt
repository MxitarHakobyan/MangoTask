package com.mango.task.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state

    private val dummyMessages = mutableListOf(
        ChatMessage("1", "Hello! How are you?", isSentByUser = false, "10:00 AM"),
        ChatMessage("2", "I'm good, thank you! How about you?", isSentByUser = true, "10:01 AM"),
        ChatMessage("3", "Doing great! Thanks for asking.", isSentByUser = false, "10:02 AM")
    )

    init {
        loadMessages()
    }

    private fun loadMessages() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            delay(1000)
            _state.value = _state.value.copy(
                messages = dummyMessages,
                isLoading = false
            )
        }
    }

    fun handleIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.SendMessage -> sendMessage(intent.message)
            is ChatIntent.LoadMessages -> loadMessages()
        }
    }

    private fun sendMessage(message: String) {
        val newMessage = ChatMessage(
            id = System.currentTimeMillis().toString(),
            content = message,
            isSentByUser = true,
            timestamp = "10:${(10..59).random()} AM"
        )
        dummyMessages.add(newMessage)
        _state.value = _state.value.copy(messages = dummyMessages)
    }
}