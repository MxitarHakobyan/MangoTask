package com.mango.task.ui.screens.authentication.enterPhoneNumber

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mango.task.data.base.Resources
import com.mango.task.data.model.request.SendAuthCodeRequest
import com.mango.task.data.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterPhoneNumberViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val usersRepository: UsersRepository,
) : ViewModel() {

    private companion object {
        const val KEY_PHONE_NUMBER = "phone_number"
        const val KEY_COUNTRY_CODE = "country_code"
        const val KEY_IS_PHONE_VALID = "is_phone_valid"
        const val KEY_IS_LOADING = "is_loading"
    }

    private val _state = MutableStateFlow(
        EnterPhoneNumberState(
            phoneNumber = savedStateHandle[KEY_PHONE_NUMBER] ?: "",
            countryCode = savedStateHandle[KEY_COUNTRY_CODE] ?: "+7",
            isPhoneValid = savedStateHandle[KEY_IS_PHONE_VALID] ?: true,
            isLoading = savedStateHandle[KEY_IS_LOADING] ?: false,
        )
    )
    val state: StateFlow<EnterPhoneNumberState> get() = _state

    private fun updateState(newState: EnterPhoneNumberState) {
        _state.value = newState
        savedStateHandle[KEY_PHONE_NUMBER] = newState.phoneNumber
        savedStateHandle[KEY_COUNTRY_CODE] = newState.countryCode
        savedStateHandle[KEY_IS_PHONE_VALID] = newState.isPhoneValid
        savedStateHandle[KEY_IS_LOADING] = newState.isLoading
    }


    private val _event = MutableSharedFlow<EnterPhoneNumberEvent>()
    val event: SharedFlow<EnterPhoneNumberEvent> = _event

    fun processIntent(intent: EnterPhoneNumberIntent) {
        when (intent) {
            is EnterPhoneNumberIntent.Submit -> handleSubmit()
            is EnterPhoneNumberIntent.PhoneNumberChanged -> {
                val isValid =
                    intent.phoneNumber.length in 7..15 && intent.phoneNumber.all { it.isDigit() }
                updateState(
                    _state.value.copy(
                        phoneNumber = intent.phoneNumber,
                        isPhoneValid = isValid
                    )
                )
            }

            is EnterPhoneNumberIntent.CountryCodeChanged -> {
                updateState(_state.value.copy(countryCode = intent.countryCode))
            }
        }
    }

    private fun handleSubmit() {
        if (_state.value.phoneNumber.isEmpty() || !_state.value.isPhoneValid) {
            updateState(_state.value.copy(isPhoneValid = false))
            return
        }

        viewModelScope.launch {
            usersRepository.sendAuthCode(SendAuthCodeRequest(phone = state.value.fullNumber()))
                .collect { result ->
                    when (result) {
                        is Resources.Loading -> {
                            updateState(_state.value.copy(isLoading = result.isLoading))
                        }

                        is Resources.Success -> {
                            result.data?.isSuccess?.let { isSuccess ->
                                if (isSuccess) {
                                    _event.emit(EnterPhoneNumberEvent.PhoneNumberSubmittedSuccessfully)
                                } else {
                                    _event.emit(
                                        EnterPhoneNumberEvent.PhoneNumberSubmissionFailed(error = "Wrong auth code")
                                    )
                                }
                                updateState(_state.value.copy(isLoading = false))
                            }
                        }

                        is Resources.Error -> {
                            updateState(_state.value.copy(isLoading = false))
                            _event.emit(
                                EnterPhoneNumberEvent.PhoneNumberSubmissionFailed(
                                    error = result.message ?: "Unknown error"
                                )
                            )
                        }
                    }
                }
        }
    }
}