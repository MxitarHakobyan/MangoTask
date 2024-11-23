package com.mango.task.ui.screens.authentication.registration

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mango.task.data.base.Resources
import com.mango.task.data.model.request.RegistrationRequest
import com.mango.task.data.repository.UsersRepository
import com.mango.task.ui.navigation.PHONE_NUMBER_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val usersRepository: UsersRepository
) : ViewModel() {

    companion object {
        private const val KEY_PHONE_NUMBER = "phoneNumber"
        private const val KEY_FULL_NAME = "fullName"
        private const val KEY_USERNAME = "username"
        private const val KEY_IS_USERNAME_VALID = "isUsernameValid"
        private const val KEY_IS_LOADING = "is_loading"
    }

    private val _state = MutableStateFlow(
        RegistrationState(
            phoneNumber = savedStateHandle[KEY_PHONE_NUMBER] ?: "",
            fullName = savedStateHandle[KEY_FULL_NAME] ?: "",
            username = savedStateHandle[KEY_USERNAME] ?: "",
            isUsernameValid = savedStateHandle[KEY_IS_USERNAME_VALID] ?: true,
            isLoading = savedStateHandle[KEY_IS_LOADING] ?: false,
        )
    )
    val state: StateFlow<RegistrationState> = _state.asStateFlow()


    private val _event = MutableSharedFlow<RegistrationEvent>()
    val event: SharedFlow<RegistrationEvent> = _event

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
                val isValid = usernameRegex.matches(intent.username) && intent.username.length >= 5
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
        updateState { it.copy(isLoading = true) }
        savedStateHandle[KEY_IS_LOADING] = true

        viewModelScope.launch {
            usersRepository.registration(
                RegistrationRequest(
                    phone = _state.value.phoneNumber,
                    name = _state.value.fullName,
                    username = _state.value.username,
                )
            ).collect { result ->
                when (result) {
                    is Resources.Loading -> {
                        updateState { it.copy(isLoading = true) }
                        savedStateHandle[KEY_IS_LOADING] = true
                    }

                    is Resources.Success -> {
                        _event.emit(RegistrationEvent.UserCreated)
                    }

                    is Resources.Error -> {
                        updateState { it.copy(isLoading = false) }
                        savedStateHandle[KEY_IS_LOADING] = false
                        _event.emit(
                            RegistrationEvent.FailedToCreateUser(
                                error = result.message ?: "Unknown Error"
                            )
                        )
                    }
                }
            }
        }
    }

    private fun updateState(update: (RegistrationState) -> RegistrationState) {
        _state.update(update)
    }
}