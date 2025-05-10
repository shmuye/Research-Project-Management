package com.project.collabrix.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.collabrix.data.dto.ProjectSummary
import com.project.collabrix.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

sealed class BrowseResearchUiState {
    object Loading : BrowseResearchUiState()
    data class Success(
        val projects: List<ProjectUiModel>,
        val searchQuery: String,
        val appliedProjectIds: Set<Int>
    ) : BrowseResearchUiState()
    data class Error(val message: String) : BrowseResearchUiState()
}

data class ProjectUiModel(
    val id: Int,
    val title: String?,
    val description: String?,
    val professorName: String?,
    val deadline: String?,
    val status: ProjectStatus,
    val isApplied: Boolean
)

enum class ProjectStatus { OPEN, CLOSED }

@HiltViewModel
class BrowseResearchViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<BrowseResearchUiState>(BrowseResearchUiState.Loading)
    val uiState: StateFlow<BrowseResearchUiState> = _uiState.asStateFlow()

    private var allProjects: List<ProjectSummary> = emptyList()
    private var appliedProjectIds: MutableSet<Int> = mutableSetOf()
    private var searchQuery: String = ""

    init {
        fetchProjects()
    }

    fun fetchProjects() {
        _uiState.value = BrowseResearchUiState.Loading
        viewModelScope.launch {
            try {
                allProjects = repository.getAllProjects()
                // Optionally, fetch applications to know which projects are already applied
                // For now, assume none applied
                updateUi()
            } catch (e: Exception) {
                _uiState.value = BrowseResearchUiState.Error(e.message ?: "Failed to load projects")
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
        updateUi()
    }

    fun applyToProject(projectId: Int) {
        viewModelScope.launch {
            try {
                repository.applyToProject(projectId)
                appliedProjectIds.add(projectId)
                updateUi()
            } catch (e: Exception) {
                // Optionally, show error
            }
        }
    }

    private fun updateUi() {
        val filtered = allProjects.filter {
            (it.title ?: "").contains(searchQuery, ignoreCase = true) ||
            (it.description ?: "").contains(searchQuery, ignoreCase = true)
        }.map { project ->
            ProjectUiModel(
                id = project.id,
                title = project.title,
                description = project.description,
                professorName = project.professorName,
                deadline = project.deadline,
                status = ProjectStatus.OPEN,
                isApplied = appliedProjectIds.contains(project.id)
            )
        }
        _uiState.value = BrowseResearchUiState.Success(
            projects = filtered,
            searchQuery = searchQuery,
            appliedProjectIds = appliedProjectIds.toSet()
        )
    }
} 