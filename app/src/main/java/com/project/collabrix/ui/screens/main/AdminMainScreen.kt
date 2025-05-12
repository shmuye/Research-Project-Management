package com.project.collabrix.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import com.project.collabrix.R
import com.project.collabrix.ui.screens.admin.projects.ProjectManagementPage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import com.project.collabrix.ui.screens.admin.usermanagement.UserManagementPage
import com.project.collabrix.ui.screens.admin.dashboard.AdminDashboardContent
import androidx.navigation.NavHostController
import androidx.compose.material.icons.automirrored.filled.ExitToApp

sealed class AdminPage(val label: String) {
    object Dashboard : AdminPage("Dashboard")
    object UserManagement : AdminPage("User Management")
    object Projects : AdminPage("Projects")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMainScreen(
    onLogout: () -> Unit,
    navController: NavHostController,
    viewModel: AdminMainViewModel = hiltViewModel()
) {
    var selectedPage by remember { mutableStateOf<AdminPage>(AdminPage.Dashboard) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AdminSidebarContent(
                selectedPage = selectedPage,
                onSelectPage = { page ->
                    selectedPage = page
                    scope.launch { drawerState.close() }
                },
                onLogout = {
                    scope.launch { drawerState.close() }
                    onLogout()
                },
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(0.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Collabrix",
                                fontFamily = FontFamily(Font(R.font.orbitron_bold)),
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color.Black
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.Black)
                        }
                    },
                    actions = {
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "App Logo",
                            modifier = Modifier.size(36.dp)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            containerColor = Color(0xFFF5F6FA),
            content = { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F6FA))
                        .padding(padding),
                    contentAlignment = Alignment.TopCenter
                ) {
                    when (selectedPage) {
                        is AdminPage.Dashboard -> AdminDashboardContent(
                            onNavigateToUserManagement = {
                                selectedPage = AdminPage.UserManagement
                            }
                        )
                        is AdminPage.UserManagement -> AdminUserManagementContent()
                        is AdminPage.Projects -> AdminProjectsContent()
                    }
                }
            }
        )
    }
}

@Composable
fun AdminSidebarContent(
    selectedPage: AdminPage,
    onSelectPage: (AdminPage) -> Unit,
    onLogout: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(240.dp)
            .fillMaxHeight()
            .background(Color(0xFFF7F7F7)),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 32.dp, 24.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Collabrix",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        AdminSidebarButton("Dashboard", selectedPage is AdminPage.Dashboard) { onSelectPage(AdminPage.Dashboard); onClose() }
        AdminSidebarButton("User Management", selectedPage is AdminPage.UserManagement) { onSelectPage(AdminPage.UserManagement); onClose() }
        AdminSidebarButton("Projects", selectedPage is AdminPage.Projects) { onSelectPage(AdminPage.Projects); onClose() }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .clickable { onLogout() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout", fontSize = 16.sp)
        }
    }
}

@Composable
fun AdminSidebarButton(text: String, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) Color(0xFFE0E0E0) else Color.Transparent
    val fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .height(40.dp)
            .clickable { onClick() },
        color = background,
        shadowElevation = if (selected) 4.dp else 0.dp,
        shape = MaterialTheme.shapes.small
    ) {
        Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.fillMaxSize()) {
            Text(
                text = text,
                fontWeight = fontWeight,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun AdminDashboardContent(navController: NavHostController) {
    AdminDashboardContent(navController = navController)
}

@Composable
fun AdminUserManagementContent() {
    UserManagementPage()
}

@Composable
fun AdminProjectsContent() {
    ProjectManagementPage()
} 