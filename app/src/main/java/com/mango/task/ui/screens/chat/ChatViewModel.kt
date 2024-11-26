package com.mango.task.ui.screens.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mango.task.ui.navigation.CHAT_ID_KEY
import com.mango.task.ui.screens.dummies.getDummyChats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state

    init {
        loadMessages()
    }

    private fun loadMessages() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            delay(1000)
            val id = checkNotNull(savedStateHandle[CHAT_ID_KEY] ?: "")
            val currentChat = getDummyChats().first { it.id == id }
            _state.value = _state.value.copy(
                messages = currentChat.messages,
                chatName = currentChat.userName,
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
        val currentMessages = _state.value.messages.toMutableList()
        currentMessages.add(newMessage)
        _state.value = _state.value.copy(messages = currentMessages)
    }
}