package com.project.collabrix.presentation

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

sealed class StudentProjectDetailState {
    object Idle : StudentProjectDetailState()
    object Loading : StudentProjectDetailState()
    data class Success(val project: ProjectDetail) : StudentProjectDetailState()
    data class Error(val message: String) : StudentProjectDetailState()
}

@HiltViewModel
class StudentProjectDetailViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {
    private val _state = MutableStateFlow<StudentProjectDetailState>(StudentProjectDetailState.Idle)
    val state: StateFlow<StudentProjectDetailState> = _state.asStateFlow()

    fun loadProjectDetail(projectId: Int) {
        _state.value = StudentProjectDetailState.Loading
        viewModelScope.launch {
            try {
                val project = repository.getProjectDetail(projectId)
                _state.value = StudentProjectDetailState.Success(project)
            } catch (e: Exception) {
                _state.value = StudentProjectDetailState.Error(e.message ?: "Failed to load project detail")
            }
        }
    }
} 