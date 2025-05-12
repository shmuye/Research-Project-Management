package com.project.collabrix.ui.screens.admin.usermanagement

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.collabrix.presentation.UserManagementViewModel
import com.project.collabrix.ui.components.UserFilterTabs
import com.project.collabrix.ui.components.UserSearchBar
import com.project.collabrix.ui.components.UserTable

@Composable
fun UserManagementPage(
    viewModel: UserManagementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            UserSearchBar(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = viewModel::setSearchQuery
            )

            // Filter Tabs
            UserFilterTabs(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = viewModel::setFilter
            )

            // Loading Indicator
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // User Table
                UserTable(
                    users = uiState.filteredUsers,
                    onDeleteUser = { userId ->
                        viewModel.deleteUser(userId)
                    }
                )
            }
        }
    }
} 