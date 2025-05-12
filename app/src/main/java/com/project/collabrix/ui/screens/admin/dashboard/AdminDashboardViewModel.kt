package com.project.collabrix.ui.screens.admin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.collabrix.data.repository.AdminDashboardStats
import com.project.collabrix.data.repository.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class AdminDashboardUiState {
    object Loading : AdminDashboardUiState()
    data class Success(val stats: AdminDashboardStats) : AdminDashboardUiState()
    data class Error(val message: String) : AdminDashboardUiState()
}

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(private val repository: AdminRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<AdminDashboardUiState>(AdminDashboardUiState.Loading)
    val uiState: StateFlow<AdminDashboardUiState> = _uiState

    fun fetchDashboardStats() {
        _uiState.value = AdminDashboardUiState.Loading
        viewModelScope.launch {
            try {
                val stats = repository.getDashboardStats()
                _uiState.value = AdminDashboardUiState.Success(stats)
            } catch (e: Exception) {
                _uiState.value = AdminDashboardUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
} 