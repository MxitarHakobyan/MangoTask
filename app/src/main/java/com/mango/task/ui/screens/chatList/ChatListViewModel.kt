package com.mango.task.ui.screens.chatList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mango.task.ui.screens.dummies.getDummyChats
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
            val dummyChats = getDummyChats()
            _state.value = ChatListState(chats = dummyChats)
        }
    }

    private fun refreshChats() {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            delay(1000)
            val refreshedChats = getDummyChats().map {
                it.copy(lastMessage = "${it.lastMessage} (updated)")
            }
            _state.value = _state.value.copy(chats = refreshedChats, isLoading = false)
        }
    }
}