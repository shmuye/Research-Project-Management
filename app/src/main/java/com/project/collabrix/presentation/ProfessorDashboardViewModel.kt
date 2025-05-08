package com.project.collabrix.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.collabrix.data.model.Project
import com.project.collabrix.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProjectUiState {
    object Loading : ProjectUiState()
    data class Success(val projects: List<Project>) : ProjectUiState()
    data class Error(val message: String) : ProjectUiState()
}

@HiltViewModel
class ProfessorDashboardViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProjectUiState>(ProjectUiState.Loading)
    val uiState: StateFlow<ProjectUiState> = _uiState

    fun fetchProjects() {
        _uiState.value = ProjectUiState.Loading
        viewModelScope.launch {
            try {
                val projects = repository.getMyProjects()
                _uiState.value = ProjectUiState.Success(projects)
            } catch (e: Exception) {
                _uiState.value = ProjectUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
} 