package com.mango.task.ui.screens.authentication.enterAuthCode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mango.task.data.base.Resources
import com.mango.task.data.model.request.CheckAuthCodeRequest
import com.mango.task.data.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthCodeViewModel @Inject constructor(
    private val repository: UsersRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    companion object {
        private const val KEY_AUTH_CODE = "auth_code"
        private const val KEY_IS_LOADING = "is_loading"
        private const val KEY_ERROR_MESSAGE = "error_message"
    }

    private val _state = MutableStateFlow(
        EnterAuthCodeState(
            authCode = savedStateHandle[KEY_AUTH_CODE] ?: "",
            isLoading = savedStateHandle[KEY_IS_LOADING] ?: false,
            errorMessage = savedStateHandle[KEY_ERROR_MESSAGE]
        )
    )
    val state: StateFlow<EnterAuthCodeState> = _state

    private val _event = MutableSharedFlow<EnterAuthCodeEvent>()
    val event: SharedFlow<EnterAuthCodeEvent> = _event

    fun handleIntent(intent: AuthCodeIntent) {
        when (intent) {
            is AuthCodeIntent.EnterAuthCode -> updateAuthCode(intent.code)
            is AuthCodeIntent.SubmitCode -> submitCode()
        }
    }

    private fun updateAuthCode(code: String) {
        val isValid = code.length == 6
        _state.value = _state.value.copy(authCode = code, isCodeValid = isValid)
        savedStateHandle[KEY_AUTH_CODE] = code
    }

    private fun submitCode() {
        if (!_state.value.isCodeValid) {
            _state.value = _state.value.copy(errorMessage = "Auth code must be 6 digits.")
            savedStateHandle[KEY_ERROR_MESSAGE] = _state.value.errorMessage
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            savedStateHandle[KEY_IS_LOADING] = true
            savedStateHandle[KEY_ERROR_MESSAGE] = null


            repository.checkAuthCode(
                checkAuthCodeRequest = CheckAuthCodeRequest(
                    phone = "",
                    code = _state.value.authCode
                )
            ).collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                        savedStateHandle[KEY_IS_LOADING] = true
                    }

                    is Resources.Success -> {
                        _event.emit(EnterAuthCodeEvent.CodeSubmittedSuccessfully)
                    }

                    is Resources.Error -> {
                        _state.value = _state.value.copy(isLoading = false)
                        savedStateHandle[KEY_IS_LOADING] = false
                        _event.emit(EnterAuthCodeEvent.CodeSubmissionFailed("Invalid code. Please try again."))
                    }
                }
            }
        }
    }
}