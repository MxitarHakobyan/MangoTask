package com.mango.task.ui.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mango.task.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_PROFILE_STATE = "profile_state"
    }

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    init {
        _state.value = savedStateHandle.get<ProfileState>(KEY_PROFILE_STATE) ?: ProfileState()
    }

    fun handleIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.EditProfile -> {
                _state.update { it.copy(isEditing = true) }
            }
            is ProfileIntent.ExitEditMode -> {
                _state.update { it.copy(isEditing = false) }
            }
            is ProfileIntent.UpdateProfile -> {
                _state.update {
                    val updatedState = it.copy(
                        fullName = intent.fullName,
                        username = intent.username,
                        dateOfBirth = intent.dateOfBirth,
                        biography = intent.biography,
                        city = intent.city,
                        avatarUrl = intent.avatarUrl,
                        phoneNumber = intent.phoneNumber,
                        isEditing = false
                    )
                    savedStateHandle[KEY_PROFILE_STATE] = updatedState
                    updatedState
                }
            }
        }
    }
}