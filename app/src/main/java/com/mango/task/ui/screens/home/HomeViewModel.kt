package com.mango.task.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mango.task.data.localStorage.prefs.SecureStorage
import com.mango.task.data.localStorage.prefs.SharedPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val secureStorage: SecureStorage,
    private val sharedPrefs: SharedPrefs,
) : ViewModel() {

    private val _viewState = MutableStateFlow(HomeViewState())
    val viewState: StateFlow<HomeViewState> = _viewState.asStateFlow()

    private val _viewIntent = MutableSharedFlow<HomeViewIntent>()
    val viewIntent: SharedFlow<HomeViewIntent> = _viewIntent.asSharedFlow()

    fun onEvent(event: HomeViewEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeViewEvent.Logout -> {
                    sharedPrefs.clear()
                    clearSecureStorage()
                    _viewIntent.emit(HomeViewIntent.NavigateToLogin)
                }
            }
        }
    }

    private fun clearSecureStorage() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            try {
                secureStorage.clear()
            } catch (e: Exception) {
                _viewIntent.emit(HomeViewIntent.ShowMessage("Failed to clear storage: ${e.message}"))
            } finally {
                _viewState.value = _viewState.value.copy(isLoading = false)
            }
        }
    }
}