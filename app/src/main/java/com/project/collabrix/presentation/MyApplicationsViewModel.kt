package com.project.collabrix.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.collabrix.data.dto.Application
import com.project.collabrix.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MyApplicationsUiState {
    object Loading : MyApplicationsUiState()
    data class Success(
        val pending: List<Application>,
        val approved: List<Application>,
        val rejected: List<Application>
    ) : MyApplicationsUiState()
    data class Error(val message: String) : MyApplicationsUiState()
}

@HiltViewModel
class MyApplicationsViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<MyApplicationsUiState>(MyApplicationsUiState.Loading)
    val uiState: StateFlow<MyApplicationsUiState> = _uiState

    fun fetchApplications() {
        _uiState.value = MyApplicationsUiState.Loading
        viewModelScope.launch {
            try {
                val applications = repository.getStudentApplications()
                _uiState.value = MyApplicationsUiState.Success(
                    pending = applications.filter { it.status == "PENDING" },
                    approved = applications.filter { it.status == "APPROVED" },
                    rejected = applications.filter { it.status == "REJECTED" }
                )
            } catch (e: Exception) {
                _uiState.value = MyApplicationsUiState.Error(e.localizedMessage ?: "Failed to load applications")
            }
        }
    }

    fun withdrawApplication(projectId: Int) {
        viewModelScope.launch {
            try {
                repository.withdrawApplication(projectId)
                fetchApplications()
            } catch (e: Exception) {
                _uiState.value = MyApplicationsUiState.Error(e.localizedMessage ?: "Failed to withdraw application")
            }
        }
    }
} 