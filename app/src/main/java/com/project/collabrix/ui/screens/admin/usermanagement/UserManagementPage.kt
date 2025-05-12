package com.project.collabrix.ui.screens.admin.usermanagement

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.collabrix.presentation.UserManagementViewModel
import com.project.collabrix.ui.components.UserFilterTabs
import com.project.collabrix.ui.components.UserSearchBar
import com.project.collabrix.ui.components.UserTable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Page Title
            Text(
                text = "User Management",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Subtitle
            Text(
                text = "Manage users and their permissions",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Search Bar
            UserSearchBar(
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = viewModel::setSearchQuery,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Filter Tabs
            UserFilterTabs(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = viewModel::setFilter,
                modifier = Modifier.padding(bottom = 16.dp)
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