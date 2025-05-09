package com.project.collabrix.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.collabrix.data.dto.ProjectDetail
import com.project.collabrix.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProjectDetailState {
    object Idle : ProjectDetailState()
    object Loading : ProjectDetailState()
    data class Success(val project: ProjectDetail) : ProjectDetailState()
    data class Error(val message: String) : ProjectDetailState()
    object Deleted : ProjectDetailState()
    data class StudentRemoved(val studentId: Int) : ProjectDetailState()
}

@HiltViewModel
class ProjectDetailViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {
    private val _state = MutableStateFlow<ProjectDetailState>(ProjectDetailState.Idle)
    val state: StateFlow<ProjectDetailState> = _state.asStateFlow()

    fun loadProjectDetail(projectId: Int) {
        _state.value = ProjectDetailState.Loading
        viewModelScope.launch {
            try {
                val project = repository.getProjectDetail(projectId)
                _state.value = ProjectDetailState.Success(project)
            } catch (e: Exception) {
                _state.value = ProjectDetailState.Error(e.message ?: "Failed to load project detail")
            }
        }
    }

    fun deleteProject(projectId: Int) {
        _state.value = ProjectDetailState.Loading
        viewModelScope.launch {
            try {
                repository.deleteProject(projectId)
                _state.value = ProjectDetailState.Deleted
            } catch (e: Exception) {
                _state.value = ProjectDetailState.Error(e.message ?: "Failed to delete project")
            }
        }
    }

    fun removeStudent(projectId: Int, studentId: Int) {
        viewModelScope.launch {
            try {
                repository.removeStudentFromProject(projectId, studentId)
                _state.value = ProjectDetailState.StudentRemoved(studentId)
                // Optionally reload project detail
                loadProjectDetail(projectId)
            } catch (e: Exception) {
                _state.value = ProjectDetailState.Error(e.message ?: "Failed to remove student")
            }
        }
    }
} 