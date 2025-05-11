package com.project.collabrix.presentation.screens.professor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.collabrix.data.dto.Application
import com.project.collabrix.presentation.ApplicationsUiState
import com.project.collabrix.presentation.ProfessorDashboardViewModel
import androidx.compose.foundation.background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationsScreen(
    viewModel: ProfessorDashboardViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.applicationsUiState.collectAsState()

    // Fetch all applications for all projects when entering the screen
    LaunchedEffect(Unit) {
        viewModel.fetchAllApplicationsForProfessor()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Project Applications") }
            )
        },
        containerColor = Color(0xFFF5F6FA)
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(Color(0xFFF5F6FA))
        ) {
            when (uiState) {
                is ApplicationsUiState.Loading, is ApplicationsUiState.StatusUpdated -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ApplicationsUiState.Error -> {
                    Text(
                        text = (uiState as ApplicationsUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is ApplicationsUiState.Success -> {
                    val applications = (uiState as ApplicationsUiState.Success).applications
                    if (applications.isEmpty()) {
                        Text(
                            text = "No applications found.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        // Group applications by project title
                        val grouped = applications.groupBy { it.projectTitle ?: "Unknown Project" }
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            grouped.forEach { (projectTitle, projectApplications) ->
                                item {
                                    Text(
                                        text = projectTitle,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }
                                items(projectApplications) { application ->
                                    ApplicationCard(
                                        application = application,
                                        onApprove = {
                                            viewModel.updateApplicationStatus(
                                                application.id, "APPROVED"
                                            )
                                        },
                                        onDecline = {
                                            viewModel.updateApplicationStatus(
                                                application.id, "REJECTED"
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ApplicationCard(
    application: Application,
    onApprove: () -> Unit,
    onDecline: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Student: ${application.student?.name ?: "Unknown"}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Email: ${application.student?.email ?: "Unknown"}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Status: ${application.status}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Project: ${application.projectTitle ?: "Unknown"}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (application.status == "PENDING") {
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E))
                    ) {
                        Text("Approve")
                    }
                    Button(
                        onClick = onDecline,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                    ) {
                        Text("Decline")
                    }
                } else if (application.status == "APPROVED") {
                    // Only show status, no Decline button
                } else if (application.status == "REJECTED") {
                    // Only show status, no Approve button
                }
            }
        }
    }
} 