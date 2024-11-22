package com.mango.task.ui.screens.authentication

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private companion object {
        const val KEY_PHONE_NUMBER = "phone_number"
        const val KEY_COUNTRY_CODE = "country_code"
        const val KEY_IS_PHONE_VALID = "is_phone_valid"
        const val KEY_IS_LOADING = "is_loading"
        const val KEY_IS_SUCCESSFUL = "is_successful"
    }
    private val _state = mutableStateOf(
        RegistrationState(
            phoneNumber = savedStateHandle[KEY_PHONE_NUMBER] ?: "",
            countryCode = savedStateHandle[KEY_COUNTRY_CODE] ?: "+7",
            isPhoneValid = savedStateHandle[KEY_IS_PHONE_VALID] ?: true,
            isLoading = savedStateHandle[KEY_IS_LOADING] ?: false,
            isRegistrationSuccessful = savedStateHandle[KEY_IS_SUCCESSFUL] ?: false
        )
    )
    val state: State<RegistrationState> get() = _state

    private fun updateState(newState: RegistrationState) {
        _state.value = newState
        savedStateHandle[KEY_PHONE_NUMBER] = newState.phoneNumber
        savedStateHandle[KEY_COUNTRY_CODE] = newState.countryCode
        savedStateHandle[KEY_IS_PHONE_VALID] = newState.isPhoneValid
        savedStateHandle[KEY_IS_LOADING] = newState.isLoading
        savedStateHandle[KEY_IS_SUCCESSFUL] = newState.isRegistrationSuccessful
    }

    fun processIntent(intent: RegistrationIntent) {
        when (intent) {
            is RegistrationIntent.Submit -> handleRegistration()
            is RegistrationIntent.PhoneNumberChanged -> {
                val isValid = intent.phoneNumber.length in 7..15 && intent.phoneNumber.all { it.isDigit() }
                updateState(_state.value.copy(phoneNumber = intent.phoneNumber, isPhoneValid = isValid))
            }
            is RegistrationIntent.CountryCodeChanged -> {
                updateState(_state.value.copy(countryCode = intent.countryCode))
            }
        }
    }

    private fun handleRegistration() {
        val currentState = _state.value

        // Validate phone number
        if (currentState.phoneNumber.isEmpty() || !currentState.isPhoneValid) {
            updateState(currentState.copy(isPhoneValid = false))
            return
        }

        updateState(currentState.copy(isLoading = true))
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000) // Simulate network call
            updateState(currentState.copy(isLoading = false, isRegistrationSuccessful = true))
        }
    }
}