package com.project.collabrix.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.collabrix.data.dto.ProjectDetail
import com.project.collabrix.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProjectManagementUiState(
    val isLoading: Boolean = false,
    val projects: List<ProjectDetail> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ProjectManagementViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProjectManagementUiState())
    val uiState: StateFlow<ProjectManagementUiState> = _uiState.asStateFlow()

    init {
        loadProjects()
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val projects = repository.getAllProjects().map { projectSummary ->
                    // Fetch detailed information for each project
                    repository.getProjectDetail(projectSummary.id)
                }
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        projects = projects,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load projects"
                ) }
            }
        }
    }

    fun deleteProject(projectId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.deleteProject(projectId)
                loadProjects() // Reload the list after deletion
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to delete project"
                ) }
            }
        }
    }
} 