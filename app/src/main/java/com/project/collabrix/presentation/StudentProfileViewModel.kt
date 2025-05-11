package com.project.collabrix.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.collabrix.data.dto.ProfileDto
import com.project.collabrix.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

sealed class StudentProfileUiState {
    object Loading : StudentProfileUiState()
    data class Success(val profile: ProfileDto, val editMode: Boolean = false) : StudentProfileUiState()
    data class Error(val message: String) : StudentProfileUiState()
    object Deleted : StudentProfileUiState()
    object Saved : StudentProfileUiState()
}

@HiltViewModel
class StudentProfileViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<StudentProfileUiState>(StudentProfileUiState.Loading)
    val uiState: StateFlow<StudentProfileUiState> = _uiState

    fun loadProfile() {
        _uiState.value = StudentProfileUiState.Loading
        viewModelScope.launch {
            try {
                val profile = repository.getProfile()
                _uiState.value = StudentProfileUiState.Success(profile)
            } catch (e: Exception) {
                _uiState.value = StudentProfileUiState.Error(e.localizedMessage ?: "Failed to load profile")
            }
        }
    }

    fun setEditMode(edit: Boolean) {
        val current = _uiState.value
        if (current is StudentProfileUiState.Success) {
            _uiState.value = current.copy(editMode = edit)
        }
    }

    fun saveProfile(profile: ProfileDto) {
        _uiState.value = StudentProfileUiState.Loading
        viewModelScope.launch {
            try {
                // Only send editable fields to backend
                val updateProfile = ProfileDto(
                    id = null, // id not needed for update
                    name = profile.name,
                    email = null, // email not editable
                    department = profile.department,
                    bio = profile.bio,
                    skills = profile.skills,
                    role = null // role not editable
                )
                val updated = repository.updateProfile(updateProfile)
                _uiState.value = StudentProfileUiState.Success(updated)
            } catch (e: Exception) {
                _uiState.value = StudentProfileUiState.Error(e.localizedMessage ?: "Failed to save profile")
            }
        }
    }

    fun deleteAccount() {
        _uiState.value = StudentProfileUiState.Loading
        viewModelScope.launch {
            try {
                repository.deleteAccount()
                _uiState.value = StudentProfileUiState.Deleted
            } catch (e: Exception) {
                _uiState.value = StudentProfileUiState.Error(e.localizedMessage ?: "Failed to delete account")
            }
        }
    }
} 