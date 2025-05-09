package com.project.collabrix.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

enum class StudentPage(val label: String) {
    Dashboard("Dashboard"),
    AvailableProjects("Available Projects"),
    MyApplications("My Applications"),
    Profile("Profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentMainScreen(
    onLogout: () -> Unit,
    viewModel: StudentMainViewModel = hiltViewModel()
) {
    val backgroundColor = Color(0xFFF5F6FA)
    val primaryColor = Color(0xFF3B82F6)
    val textColor = Color(0xFF1F2937)
    
    var selectedPage by remember { mutableStateOf(StudentPage.Dashboard) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Student Menu",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                Divider()
                StudentPage.values().forEach { page ->
                    NavigationDrawerItem(
                        label = { Text(page.label) },
                        selected = selectedPage == page,
                        onClick = {
                            selectedPage = page
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = when (page) {
                                    StudentPage.Dashboard -> Icons.Default.DateRange
                                    StudentPage.AvailableProjects -> Icons.Default.List
                                    StudentPage.MyApplications -> Icons.Default.AccountBox
                                    StudentPage.Profile -> Icons.Default.Person
                                },
                                contentDescription = page.label
                            )
                        }
                    )
                }
                Divider()
                NavigationDrawerItem(
                    label = { Text("Logout") },
                    selected = false,
                    onClick = onLogout,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selectedPage.label) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(paddingValues)
            ) {
                when (selectedPage) {
                    StudentPage.Dashboard -> DashboardContent()
                    StudentPage.AvailableProjects -> AvailableProjectsContent()
                    StudentPage.MyApplications -> MyApplicationsContent()
                    StudentPage.Profile -> ProfileContent()
                }
            }
        }
    }
}

@Composable
private fun DashboardContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to your Student Dashboard!",
            fontSize = 18.sp,
            color = Color(0xFF1F2937),
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Your Applications",
                    fontSize = 20.sp,
                    color = Color(0xFF1F2937)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No applications yet",
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun AvailableProjectsContent() {
    // TODO: Implement available projects list
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Available Projects Coming Soon")
    }
}

@Composable
private fun MyApplicationsContent() {
    // TODO: Implement my applications list
    Box(modifier = Modifier.fillMaxSize()) {
        Text("My Applications Coming Soon")
    }
}

@Composable
private fun ProfileContent() {
    // TODO: Implement profile view
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Profile Coming Soon")
    }
} 