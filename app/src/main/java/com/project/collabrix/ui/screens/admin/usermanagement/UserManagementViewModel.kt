package com.project.collabrix.ui.screens.admin.usermanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.collabrix.data.dto.UserFilter
import com.project.collabrix.data.dto.UserManagementDto
import com.project.collabrix.data.repository.UserManagementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserManagementUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val users: List<UserManagementDto> = emptyList(),
    val currentFilter: UserFilter = UserFilter.ALL_USERS,
    val searchQuery: String = ""
)

@HiltViewModel
class UserManagementViewModel @Inject constructor(
    private val repository: UserManagementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserManagementUiState())
    val uiState: StateFlow<UserManagementUiState> = _uiState

    init {
        loadUsers()
    }

    fun setFilter(filter: UserFilter) {
        _uiState.update { it.copy(currentFilter = filter) }
        loadUsers()
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val users = when (_uiState.value.currentFilter) {
                    UserFilter.ALL_USERS -> repository.getAllUsers()
                    UserFilter.STUDENTS -> repository.getStudents()
                    UserFilter.PROFESSORS -> repository.getProfessors()
                }
                _uiState.update { it.copy(isLoading = false, users = users) }
                filterUsers()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun filterUsers() {
        val query = _uiState.value.searchQuery.lowercase()
        if (query.isEmpty()) {
            loadUsers()
            return
        }

        _uiState.update { state ->
            state.copy(
                users = state.users.filter { user ->
                    user.name.lowercase().contains(query) ||
                    user.email.lowercase().contains(query) ||
                    user.department.lowercase().contains(query)
                }
            )
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.deleteUser(userId).fold(
                onSuccess = { loadUsers() },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }
} 