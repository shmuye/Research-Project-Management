package com.project.collabrix.ui.screens.admin.usermanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.collabrix.data.dto.UserFilter
import com.project.collabrix.data.dto.UserManagementDto
import com.project.collabrix.data.repository.UserManagementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserManagementUiState(
    val isLoading: Boolean = false,
    val users: List<UserManagementDto> = emptyList(),
    val filteredUsers: List<UserManagementDto> = emptyList(),
    val selectedFilter: UserFilter = UserFilter.ALL_USERS,
    val searchQuery: String = "",
    val error: String? = null
)

@HiltViewModel
class UserManagementViewModel @Inject constructor(
    private val repository: UserManagementRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserManagementUiState())
    val uiState: StateFlow<UserManagementUiState> = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val users = when (_uiState.value.selectedFilter) {
                    UserFilter.ALL_USERS -> repository.getAllUsers()
                    UserFilter.STUDENTS -> repository.getStudents()
                    UserFilter.PROFESSORS -> repository.getProfessors()
                }
                _uiState.update { state ->
                    state.copy(
                        users = users,
                        filteredUsers = filterUsers(users, state.searchQuery),
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = "Failed to load users: ${e.message}"
                ) }
            }
        }
    }

    fun onFilterSelected(filter: UserFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
        loadUsers()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                filteredUsers = filterUsers(state.users, query)
            )
        }
    }

    private fun filterUsers(users: List<UserManagementDto>, query: String): List<UserManagementDto> {
        return if (query.isBlank()) {
            users
        } else {
            users.filter { user ->
                user.name?.contains(query, ignoreCase = true) == true ||
                user.email.contains(query, ignoreCase = true) ||
                user.department?.contains(query, ignoreCase = true) == true
            }
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.deleteUser(userId)
                loadUsers() // Reload the list after deletion
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = "Failed to delete user: ${e.message}"
                ) }
            }
        }
    }
} 