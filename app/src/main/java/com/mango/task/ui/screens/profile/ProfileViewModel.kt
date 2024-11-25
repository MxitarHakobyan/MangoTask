package com.mango.task.ui.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mango.task.data.base.Resources
import com.mango.task.data.model.request.Avatar
import com.mango.task.data.model.request.ProfileUpdateRequest
import com.mango.task.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_IS_LOADING = "is_loading"
    }

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

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
                        ProfileUpdateRequest(
                            name = intent.fullName,
                            username = intent.username,
                            birthday = intent.dateOfBirth.ifEmpty { null },
                            city = intent.city,
                            status = intent.biography,
                            avatar = Avatar("", intent.avatarUrl)
                        )
                    ).collect { result ->
                        when (result) {
                            is Resources.Loading -> {
                                updateState { it.copy(isLoading = true) }
                                savedStateHandle[KEY_IS_LOADING] = true
                            }

                            is Resources.Success -> {
                                if (result.data == true) {
                                    fetchProfileData(forceUpdate = false)
                                    updateState {
                                        it.copy(isEditing = false)
                                    }
                                } else {
                                    updateState {
                                        it.copy(
                                            isLoading = false,
                                            errorMessage = "Saving failed",
                                            isEditing = false,
                                        )
                                    }
                                }
                            }

                            is Resources.Error -> {
                                updateState {
                                    it.copy(
                                        isLoading = false,
                                        errorMessage = result.message ?: "Unknown error"
                                    )
                                }
                                savedStateHandle[KEY_IS_LOADING] = false
                            }
                        }
                    }
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
                            updateState { it.copy(isLoading = true) }
                            savedStateHandle[KEY_IS_LOADING] = true
                        }

                        is Resources.Success -> {
                            result.data?.let { data ->
                                _state.value = data
                            }
                        }

                        is Resources.Error -> {
                            updateState {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = result.message ?: "Unknown error"
                                )
                            }
                            savedStateHandle[KEY_IS_LOADING] = false
                        }
                    }
                }
        }
    }

    private fun updateState(update: (ProfileState) -> ProfileState) {
        _state.update(update)
    }
}