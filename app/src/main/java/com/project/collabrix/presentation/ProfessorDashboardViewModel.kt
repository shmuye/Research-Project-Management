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
import kotlinx.coroutines.flow.asStateFlow
import com.project.collabrix.data.dto.ProjectSummary

sealed class ProjectUiState {
    object Loading : ProjectUiState()
    data class Success(val projects: List<ProjectSummary>) : ProjectUiState()
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

    private val _pendingApplicationsCount = MutableStateFlow(0)
    val pendingApplicationsCount: StateFlow<Int> = _pendingApplicationsCount.asStateFlow()

    private val _totalApprovedStudentsCount = MutableStateFlow(0)
    val totalApprovedStudentsCount: StateFlow<Int> = _totalApprovedStudentsCount.asStateFlow()

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
                // Compute counts
                _pendingApplicationsCount.value = allApplications.count { it.status == "PENDING" }
                _totalApprovedStudentsCount.value = allApplications.count { it.status == "APPROVED" }
            } catch (e: Exception) {
                _applicationsUiState.value = ApplicationsUiState.Error(e.localizedMessage ?: "Unknown error")
                _pendingApplicationsCount.value = 0
                _totalApprovedStudentsCount.value = 0
            }
        }
    }
} 