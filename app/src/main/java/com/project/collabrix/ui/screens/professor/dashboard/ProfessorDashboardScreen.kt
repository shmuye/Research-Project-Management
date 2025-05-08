package com.project.collabrix.ui.screens.professor.dashboard

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.collabrix.presentation.ProfessorDashboardViewModel
import com.project.collabrix.presentation.ProjectUiState
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.project.collabrix.R
import com.project.collabrix.data.local.UserPreferences
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import com.project.collabrix.data.dto.Project

enum class ProfessorPage(val label: String) {
    Dashboard("Dashboard"),
    MyProjects("My Projects"),
    Applications("Applications"),
    Profile("Profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessorDashboardScreen(
    onLogout: () -> Unit = {},
    onNewProject: () -> Unit = {}
) {
    var selectedPage by remember { mutableStateOf(ProfessorPage.Dashboard) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val userFlow = userPreferences.getCurrentUser().collectAsState(initial = null)
    val userName = userFlow.value?.let { it.firstName + if (it.lastName.isNotBlank()) " " + it.lastName else "" } ?: "Professor"
    var showCreateProjectScreen by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SidebarContent(
                selectedPage = selectedPage,
                onSelectPage = { selectedPage = it },
                onLogout = { scope.launch { drawerState.close() }; onLogout() },
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                if (!showCreateProjectScreen) {
                    TopAppBar(
                        title = {
                            Column(modifier = Modifier.fillMaxWidth().padding(0.dp)) {
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
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.fillMaxWidth().padding(0.dp))
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
                }
            },
            content = { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(padding),
                    contentAlignment = Alignment.TopCenter
                ) {
                    if (showCreateProjectScreen) {
                        CreateProjectScreen(onBack = { showCreateProjectScreen = false })
                    } else {
                        DashboardMainContent(
                            userName = userName,
                            onNewProject = { showCreateProjectScreen = true }
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun DashboardMainContent(userName: String, onNewProject: () -> Unit) {
    val viewModel: ProfessorDashboardViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit) { viewModel.fetchProjects() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Dashboard",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (userName.isBlank()) "Welcome back, Professor" else "Welcome back, $userName",
            fontSize = 16.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onNewProject,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.height(44.dp)
        ) {
            Text("+ New Project", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(20.dp))
        // Stats Section
        StatCard(title = "Active Projects", value = getActiveProjectsCount(uiState))
        Spacer(modifier = Modifier.height(12.dp))
        StatCard(title = "Student Applications", value = "-", placeholder = true) // TODO: Connect to backend
        Spacer(modifier = Modifier.height(12.dp))
        StatCard(title = "Total Students", value = "-", placeholder = true) // TODO: Connect to backend
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Your Research Projects",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(12.dp))
        when (uiState) {
            is ProjectUiState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }
            is ProjectUiState.Error -> {
                val message = (uiState as ProjectUiState.Error).message
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("Error: $message", color = Color.Red, fontSize = 16.sp)
                }
            }
            is ProjectUiState.Success -> {
                val projects = (uiState as ProjectUiState.Success).projects
                if (projects.isEmpty()) {
                    Text("No projects found.", color = Color.Gray, fontSize = 16.sp)
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        projects.forEach { project ->
                            ProjectCardFigmaStyle(project = project)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(title: String, value: Any, placeholder: Boolean = false) {
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
                text = if (placeholder) "-" else value.toString(),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
        }
    }
}

private fun getActiveProjectsCount(uiState: ProjectUiState): Int =
    if (uiState is ProjectUiState.Success) uiState.projects.size else 0

@Composable
private fun ProjectCardFigmaStyle(project: Project) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = ButtonDefaults.outlinedButtonBorder,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                project.title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                project.description,
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO: View project details */ },
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

@Composable
private fun SidebarContent(
    selectedPage: ProfessorPage,
    onSelectPage: (ProfessorPage) -> Unit,
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
        SidebarButton("Dashboard", selectedPage == ProfessorPage.Dashboard) { onSelectPage(
            ProfessorPage.Dashboard
        ); onClose() }
        SidebarButton("My Projects", selectedPage == ProfessorPage.MyProjects) { onSelectPage(
            ProfessorPage.MyProjects
        ); onClose() }
        SidebarButton("Applications", selectedPage == ProfessorPage.Applications) { onSelectPage(
            ProfessorPage.Applications
        ); onClose() }
        SidebarButton("Profile", selectedPage == ProfessorPage.Profile) { onSelectPage(ProfessorPage.Profile); onClose() }
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
private fun SidebarButton(text: String, selected: Boolean, onClick: () -> Unit) {
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