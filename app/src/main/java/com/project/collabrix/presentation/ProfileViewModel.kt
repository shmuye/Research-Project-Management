package com.project.collabrix.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.collabrix.data.dto.UserProfile
import com.project.collabrix.data.dto.UserProfileUpdate
import com.project.collabrix.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val profile: UserProfile) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
    object Deleted : ProfileUiState()
    object Saved : ProfileUiState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadProfile() {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            try {
                val profile = repository.getMyProfile()
                _uiState.value = ProfileUiState.Success(profile)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.localizedMessage ?: "Failed to load profile")
            }
        }
    }

    fun saveProfile(update: UserProfileUpdate) {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            try {
                val profile = repository.updateMyProfile(update)
                _uiState.value = ProfileUiState.Success(profile)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.localizedMessage ?: "Failed to save profile")
            }
        }
    }

    fun deleteAccount() {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            try {
                repository.deleteMyAccount()
                _uiState.value = ProfileUiState.Deleted
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.localizedMessage ?: "Failed to delete account")
            }
        }
    }
} 