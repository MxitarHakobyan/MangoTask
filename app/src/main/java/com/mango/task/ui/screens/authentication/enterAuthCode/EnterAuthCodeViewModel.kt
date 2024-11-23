package com.mango.task.ui.screens.authentication.enterAuthCode

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mango.task.data.base.Resources
import com.mango.task.data.model.request.CheckAuthCodeRequest
import com.mango.task.data.repository.UsersRepository
import com.mango.task.ui.navigation.PHONE_NUMBER_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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
        private const val KEY_PHONE_NUMBER = "phone_number"
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

    init {
        val phoneNumber = checkNotNull(savedStateHandle[PHONE_NUMBER_KEY] ?: "")
        updateState { it.copy(phoneNumber = phoneNumber) }
        savedStateHandle[KEY_PHONE_NUMBER] = phoneNumber
    }

    fun handleIntent(intent: AuthCodeIntent) {
        when (intent) {
            is AuthCodeIntent.EnterAuthCode -> updateAuthCode(intent.code)
            is AuthCodeIntent.SubmitCode -> submitCode()
        }
    }

    private fun updateAuthCode(code: String) {
        val isValid = code.length == 6
        updateState { it.copy(authCode = code, isCodeValid = isValid) }
        savedStateHandle[KEY_AUTH_CODE] = code
    }

    private fun submitCode() {
        if (!_state.value.isCodeValid) {
            updateState { it.copy(errorMessage = "Auth code must be 6 digits.") }
            savedStateHandle[KEY_ERROR_MESSAGE] = _state.value.errorMessage
            return
        }

        viewModelScope.launch {
            updateState { it.copy(isLoading = true, errorMessage = null) }
            savedStateHandle[KEY_IS_LOADING] = true
            savedStateHandle[KEY_ERROR_MESSAGE] = null


            repository.checkAuthCode(
                checkAuthCodeRequest = CheckAuthCodeRequest(
                    phone = state.value.phoneNumber,
                    code = _state.value.authCode
                )
            ).collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        updateState { it.copy(isLoading = true) }
                        savedStateHandle[KEY_IS_LOADING] = true
                    }

                    is Resources.Success -> {
                        _event.emit(EnterAuthCodeEvent.CodeSubmittedSuccessfully)
                    }

                    is Resources.Error -> {
                        updateState { it.copy(isLoading = false) }
                        savedStateHandle[KEY_IS_LOADING] = false
                        _event.emit(
                            EnterAuthCodeEvent.CodeSubmissionFailed(
                                error = result.message ?: "Unknown Error"
                            )
                        )
                    }
                }
            }
        }
    }

    private fun updateState(update: (EnterAuthCodeState) -> EnterAuthCodeState) {
        _state.update(update)
    }
}