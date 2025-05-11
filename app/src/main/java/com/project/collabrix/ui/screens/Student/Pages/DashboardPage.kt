package com.project.collabrix.ui.screens.Student.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.project.collabrix.R
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.collabrix.presentation.StudentDashboardViewModel
import com.project.collabrix.presentation.StudentDashboardUiState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.project.collabrix.ui.screens.Student.Pages.MyApplicationsScreen

// Enum for navigation
enum class StudentPage(val label: String) {
    Dashboard("Dashboard"),
    BrowseResearch("Browse Research"),
    MyApplications("My Applications"),
    Profile("Profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    onLogout: () -> Unit = {},
    navController: NavHostController
) {
    var selectedPage by remember { mutableStateOf(StudentPage.Dashboard) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val dashboardViewModel: StudentDashboardViewModel = hiltViewModel()
    val uiState by dashboardViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        dashboardViewModel.fetchDashboardData()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            StudentSidebarContent(
                selectedPage = selectedPage,
                onSelectPage = { page ->
                    if (page == StudentPage.Profile) {
                        navController.navigate("studentProfile")
                    } else {
                        selectedPage = page
                    }
                },
                onLogout = {
                    scope.launch { drawerState.close() }
                    navController.navigate("landing") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
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
                            Icon(Icons.Default.Menu, contentDescription = "Open navigation drawer")
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
                        StudentPage.Dashboard -> {
                            when (uiState) {
                                is StudentDashboardUiState.Loading -> {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                                is StudentDashboardUiState.Error -> {
                                    Text(
                                        text = (uiState as StudentDashboardUiState.Error).message,
                                        color = Color.Red,
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                                is StudentDashboardUiState.Success -> {
                                    val data = uiState as StudentDashboardUiState.Success
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(0.dp)
                                    ) {
                                        item {
                                            Text(
                                                text = "Student Dashboard",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 20.sp,
                                                color = Color.Black
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Welcome back, ${data.name}",
                                                fontSize = 16.sp,
                                                color = Color.Black
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                            StatCard(title = "Applied Projects", value = data.appliedCount)
                                            Spacer(modifier = Modifier.height(12.dp))
                                            StatCard(title = "Active Projects", value = data.activeCount)
                                            Spacer(modifier = Modifier.height(24.dp))
                                            Text(
                                                text = "Your Active Projects",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp,
                                                color = Color.Black
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))
                                            if (data.activeProjects.isEmpty()) {
                                                Text("No active projects found.", color = Color.Gray, fontSize = 16.sp)
                                            }
                                        }
                                        if (data.activeProjects.isNotEmpty()) {
                                            items(data.activeProjects) { project ->
                                                ProjectCardFigmaStyle(project = project, navController = navController)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        StudentPage.BrowseResearch -> {
                            BrowseResearchScreen()
                        }
                        StudentPage.MyApplications -> {
                            MyApplicationsScreen()
                        }
                        // Add other pages as needed
                        else -> {}
                    }
                }
            }
        )
    }
}

@Composable
fun StudentSidebarContent(
    selectedPage: StudentPage,
    onSelectPage: (StudentPage) -> Unit,
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
        StudentSidebarButton("Dashboard", selectedPage == StudentPage.Dashboard) { onSelectPage(StudentPage.Dashboard); onClose() }
        StudentSidebarButton("Browse Research", selectedPage == StudentPage.BrowseResearch) { onSelectPage(StudentPage.BrowseResearch); onClose() }
        StudentSidebarButton("My Applications", selectedPage == StudentPage.MyApplications) { onSelectPage(StudentPage.MyApplications); onClose() }
        StudentSidebarButton("Profile", selectedPage == StudentPage.Profile) { onSelectPage(StudentPage.Profile); onClose() }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .clickable { onLogout() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout", fontSize = 16.sp)
        }
    }
}

@Composable
private fun StudentSidebarButton(text: String, selected: Boolean, onClick: () -> Unit) {
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
private fun StatCard(title: String, value: Any) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = ButtonDefaults.outlinedButtonBorder,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, color = Color.Black, fontWeight = FontWeight.Medium, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value.toString(),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
        }
    }
}

@Composable
private fun ProjectCardFigmaStyle(project: com.project.collabrix.data.dto.ProjectSummary, navController: NavHostController) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = ButtonDefaults.outlinedButtonBorder,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                project.title ?: "Untitled Project",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                project.professorName?.let { "Led By $it" } ?: "",
                color = Color(0xFF22C55E),
                fontSize = 14.sp
            )
            Text(
                "Active",
                color = Color(0xFF22C55E),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                project.description ?: "",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("studentProjectDetail/${project.id}") },
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                border = ButtonDefaults.outlinedButtonBorder,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text("View Details", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
} 