package com.project.collabrix.ui.screens.Student.Pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.collabrix.presentation.MyApplicationsViewModel
import com.project.collabrix.presentation.MyApplicationsUiState

@Composable
fun MyApplicationsScreen() {
    val viewModel: MyApplicationsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Pending", "Approved", "Rejected")

    // State for confirmation dialog
    var withdrawProjectId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchApplications()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "My Applications",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        TabRow(selectedTabIndex = selectedTab, containerColor = Color.White) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (uiState) {
            is MyApplicationsUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is MyApplicationsUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text((uiState as MyApplicationsUiState.Error).message, color = Color.Red)
                }
            }
            is MyApplicationsUiState.Success -> {
                val data = uiState as MyApplicationsUiState.Success
                val applications = when (selectedTab) {
                    0 -> data.pending
                    1 -> data.approved
                    2 -> data.rejected
                    else -> emptyList()
                }
                if (applications.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No applications found.", color = Color.Gray)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(applications) { application ->
                            ApplicationCard(
                                application = application,
                                showWithdraw = selectedTab == 0,
                                onWithdraw = { withdrawProjectId = application.projectId }
                            )
                        }
                    }
                }
            }
        }
    }

    // Confirmation dialog
    if (withdrawProjectId != null) {
        AlertDialog(
            onDismissRequest = { withdrawProjectId = null },
            title = { Text("Withdraw Application") },
            text = { Text("Are you sure you want to withdraw your application for this project?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.withdrawApplication(withdrawProjectId!!)
                        withdrawProjectId = null
                    }
                ) { Text("Yes, Withdraw") }
            },
            dismissButton = {
                TextButton(onClick = { withdrawProjectId = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun ApplicationCard(
    application: com.project.collabrix.data.dto.Application,
    showWithdraw: Boolean,
    onWithdraw: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = Color.White,
        shadowElevation = 2.dp,
        border = ButtonDefaults.outlinedButtonBorder,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                application.project?.title
                    ?: application.projectTitle
                    ?: "Untitled Project",
                fontWeight = FontWeight.Bold, fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Status: ${application.status}", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Deadline: ${application.project?.deadline?.substring(0, 10) ?: "N/A"}", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            if (showWithdraw) {
                Button(
                    onClick = onWithdraw,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text("Withdraw", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
} 
