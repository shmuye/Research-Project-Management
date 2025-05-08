package com.project.collabrix.ui.screens.main.professor.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.collabrix.data.dto.CreateProjectRequest
import com.project.collabrix.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {
    fun createProject(
        req: CreateProjectRequest,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.createProject(req)
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "Failed to create project.")
            }
        }
    }
} 