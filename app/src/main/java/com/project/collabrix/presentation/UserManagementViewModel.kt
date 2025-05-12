package com.project.collabrix.presentation

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
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val users = when (_uiState.value.selectedFilter) {
                    UserFilter.ALL_USERS -> repository.getAllUsers()
                    UserFilter.STUDENTS -> repository.getStudents()
                    UserFilter.PROFESSORS -> repository.getProfessors()
                }
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        users = users,
                        filteredUsers = filterUsers(users, state.searchQuery)
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load users"
                ) }
            }
        }
    }

    fun setFilter(filter: UserFilter) {
        if (_uiState.value.selectedFilter != filter) {
            _uiState.update { it.copy(selectedFilter = filter) }
            loadUsers()
        }
    }

    fun setSearchQuery(query: String) {
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
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.deleteUser(userId)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            users = state.users.filter { it.id != userId },
                            filteredUsers = state.filteredUsers.filter { it.id != userId }
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to delete user"
                    ) }
                }
        }
    }
} 