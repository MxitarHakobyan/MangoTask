package com.mango.task.ui.screens.authentication.registration

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mango.task.ui.navigation.PHONE_NUMBER_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_PHONE_NUMBER = "phoneNumber"
        private const val KEY_FULL_NAME = "fullName"
        private const val KEY_USERNAME = "username"
        private const val KEY_IS_USERNAME_VALID = "isUsernameValid"
    }

    private val _state = MutableStateFlow(
        RegistrationState(
            phoneNumber = savedStateHandle[KEY_PHONE_NUMBER] ?: "",
            fullName = savedStateHandle[KEY_FULL_NAME] ?: "",
            username = savedStateHandle[KEY_USERNAME] ?: "",
            isUsernameValid = savedStateHandle[KEY_IS_USERNAME_VALID] ?: true
        )
    )
    val state: StateFlow<RegistrationState> = _state.asStateFlow()

    private val usernameRegex = Regex("^[A-Za-z0-9_-]+$")


    init {
        val phoneNumber = checkNotNull(savedStateHandle[PHONE_NUMBER_KEY] ?: "")
        updateState { it.copy(phoneNumber = phoneNumber) }
        savedStateHandle[KEY_PHONE_NUMBER] = phoneNumber
    }

    fun handleIntent(intent: RegistrationIntent) {
        when (intent) {
            is RegistrationIntent.EnterFullName -> {
                updateState { it.copy(fullName = intent.fullName) }
                savedStateHandle[KEY_FULL_NAME] = intent.fullName
                validateForm()
            }

            is RegistrationIntent.EnterUsername -> {
                val isValid = usernameRegex.matches(intent.username)
                updateState {
                    it.copy(
                        username = intent.username,
                        isUsernameValid = isValid
                    )
                }
                savedStateHandle[KEY_USERNAME] = intent.username
                savedStateHandle[KEY_IS_USERNAME_VALID] = isValid
                validateForm()
            }

            is RegistrationIntent.Register -> {
                registerUser()
            }
        }
    }

    private fun validateForm() {
        _state.update { currentState ->
            val isFormValid = currentState.fullName.isNotBlank() &&
                    currentState.username.isNotBlank() &&
                    currentState.isUsernameValid
            currentState.copy(isRegisterEnabled = isFormValid)
        }
    }

    private fun registerUser() {
        val currentState = _state.value
        if (currentState.isRegisterEnabled) {
            updateState { it.copy(errorMessage = null) }
        } else {
            updateState { it.copy(errorMessage = "Registration failed. Please check the inputs.") }
        }
    }

    private fun updateState(update: (RegistrationState) -> RegistrationState) {
        _state.update(update)
    }
}