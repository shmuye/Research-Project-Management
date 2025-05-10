package com.project.collabrix.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.collabrix.data.dto.Application
import com.project.collabrix.data.dto.ProjectSummary
import com.project.collabrix.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class StudentDashboardUiState {
    object Loading : StudentDashboardUiState()
    data class Success(
        val name: String,
        val appliedCount: Int,
        val activeCount: Int,
        val activeProjects: List<ProjectSummary>
    ) : StudentDashboardUiState()
    data class Error(val message: String) : StudentDashboardUiState()
}

@HiltViewModel
class StudentDashboardViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<StudentDashboardUiState>(StudentDashboardUiState.Loading)
    val uiState: StateFlow<StudentDashboardUiState> = _uiState.asStateFlow()

    fun fetchDashboardData() {
        _uiState.value = StudentDashboardUiState.Loading
        viewModelScope.launch {
            try {
                // Fetch student profile (assume /profile returns { name: ... })
                val profile = repository.getProfile()
                val name = profile.name ?: "Student"

                val applications = repository.getStudentApplications()
                val appliedCount = applications.count { it.status == "PENDING" }
                val activeProjects = applications.filter { it.status == "APPROVED" }.mapNotNull { it.project }
                val activeCount = activeProjects.size

                _uiState.value = StudentDashboardUiState.Success(
                    name = name,
                    appliedCount = appliedCount,
                    activeCount = activeCount,
                    activeProjects = activeProjects
                )
            } catch (e: Exception) {
                _uiState.value = StudentDashboardUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
} 