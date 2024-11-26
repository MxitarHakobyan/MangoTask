package com.mango.task.ui.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mango.task.data.base.Resources
import com.mango.task.data.localStorage.prefs.SecureStorage
import com.mango.task.data.localStorage.prefs.SharedPrefs
import com.mango.task.data.model.request.Avatar
import com.mango.task.data.model.request.ProfileUpdateRequest
import com.mango.task.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val sharedPrefs: SharedPrefs,
    private val secureStorage: SecureStorage,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    companion object {
        private const val KEY_IS_LOADING = "is_loading"
    }

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    private val _viewEvent = MutableSharedFlow<ProfileEvent>()
    val viewEvent: SharedFlow<ProfileEvent> = _viewEvent.asSharedFlow()

    init {
        fetchProfileData(forceUpdate = true)
    }

    fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.EditProfile -> {
                _state.update { it.copy(isEditing = true) }
            }

            is ProfileIntent.RefreshProfile -> {
                fetchProfileData(forceUpdate = true)
            }

            is ProfileIntent.ExitEditMode -> {
                _state.update { it.copy(isEditing = false) }
            }

            is ProfileIntent.UpdateProfile -> {
                viewModelScope.launch {
                    profileRepository.updateProfile(
                        intent.run {
                            ProfileUpdateRequest(
                                name = fullName,
                                username = state.value.username,
                                birthday = dateOfBirth.ifEmpty { null },
                                city = city,
                                status = biography,
                                avatar = if (base64.isNullOrEmpty()) null else Avatar(
                                    filename = "avatar",
                                    base64 = base64
                                )
                            )
                        }
                    ).collect { result ->
                        when (result) {
                            is Resources.Loading -> {
                                updateState { it.copy(isLoading = result.isLoading) }
                                savedStateHandle[KEY_IS_LOADING] = true
                            }

                            is Resources.Success -> {
                                if (result.data == true) {
                                    fetchProfileData(forceUpdate = false)
                                    updateState {
                                        it.copy(isEditing = false)
                                    }
                                } else {
                                    _viewEvent.emit(
                                        ProfileEvent.ShowMessage(
                                            message = "Saving failed"
                                        )
                                    )
                                    updateState {
                                        it.copy(
                                            isLoading = false,
                                            isEditing = false,
                                        )
                                    }
                                }
                            }

                            is Resources.Error -> {
                                updateState { it.copy(isEditing = false, isLoading = false) }
                                savedStateHandle[KEY_IS_LOADING] = false
                                _viewEvent.emit(
                                    ProfileEvent.ShowMessage(
                                        message = result.message ?: "Unknown error"
                                    )
                                )
                            }
                        }
                    }
                }
            }

            ProfileIntent.Logout -> {
                viewModelScope.launch {
                    sharedPrefs.clear()
                    clearSecureStorage()
                    _viewEvent.emit(ProfileEvent.NavigateToLogin)
                }
            }
        }
    }

    private fun fetchProfileData(forceUpdate: Boolean = false) {
        viewModelScope.launch {
            profileRepository.fetchProfile(forceUpdate = forceUpdate)
                .collect { result ->
                    when (result) {
                        is Resources.Loading -> {
                            updateState { it.copy(isLoading = result.isLoading) }
                            savedStateHandle[KEY_IS_LOADING] = true
                        }

                        is Resources.Success -> {
                            result.data?.let { data ->
                                _state.value = data
                            }
                        }

                        is Resources.Error -> {
                            updateState { it.copy(isLoading = false) }
                            _viewEvent.emit(
                                ProfileEvent.ShowMessage(
                                    message = result.message ?: "Unknown error"
                                )
                            )
                            savedStateHandle[KEY_IS_LOADING] = false
                        }
                    }
                }
        }
    }

    private fun clearSecureStorage() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            try {
                secureStorage.clearStorage()
            } catch (e: Exception) {
                _viewEvent.emit(
                    ProfileEvent.ShowMessage(
                        message = "Failed to clear storage: ${e.message}"
                    )
                )
            } finally {
                updateState { it.copy(isLoading = false) }
            }
        }
    }

    private fun updateState(update: (ProfileState) -> ProfileState) {
        _state.update(update)
    }
}