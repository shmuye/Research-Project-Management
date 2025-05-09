package com.project.collabrix.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.collabrix.data.dto.Project
import com.project.collabrix.data.dto.Application
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

sealed class ApplicationsUiState {
    object Loading : ApplicationsUiState()
    data class Success(val applications: List<Application>) : ApplicationsUiState()
    data class Error(val message: String) : ApplicationsUiState()
    data class StatusUpdated(val application: Application) : ApplicationsUiState()
}

@HiltViewModel
class ProfessorDashboardViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProjectUiState>(ProjectUiState.Loading)
    val uiState: StateFlow<ProjectUiState> = _uiState

    private val _applicationsUiState = MutableStateFlow<ApplicationsUiState>(ApplicationsUiState.Loading)
    val applicationsUiState: StateFlow<ApplicationsUiState> = _applicationsUiState

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

    fun fetchApplicationsForProject(projectId: Int) {
        _applicationsUiState.value = ApplicationsUiState.Loading
        viewModelScope.launch {
            try {
                val applications = repository.getProjectApplications(projectId)
                _applicationsUiState.value = ApplicationsUiState.Success(applications)
            } catch (e: Exception) {
                _applicationsUiState.value = ApplicationsUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun updateApplicationStatus(applicationId: Int, status: String) {
        viewModelScope.launch {
            try {
                val updated = repository.updateApplicationStatus(applicationId, status)
                _applicationsUiState.value = ApplicationsUiState.StatusUpdated(updated)
                // Always refresh the list for all projects
                fetchAllApplicationsForProfessor()
            } catch (e: Exception) {
                _applicationsUiState.value = ApplicationsUiState.Error(e.localizedMessage ?: "Failed to update status")
            }
        }
    }

    fun fetchAllApplicationsForProfessor() {
        _applicationsUiState.value = ApplicationsUiState.Loading
        viewModelScope.launch {
            try {
                val projects = repository.getMyProjects()
                val allApplications = projects.flatMap { project ->
                    repository.getProjectApplications(project.id)
                        .map { it.copy(projectTitle = project.title) }
                }
                _applicationsUiState.value = ApplicationsUiState.Success(allApplications)
            } catch (e: Exception) {
                _applicationsUiState.value = ApplicationsUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
} 