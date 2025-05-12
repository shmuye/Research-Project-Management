package com.project.collabrix.ui.screens.Student.Pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.collabrix.presentation.StudentProfileViewModel
import com.project.collabrix.presentation.StudentProfileUiState
import com.project.collabrix.data.dto.ProfileDto
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import com.project.collabrix.ui.screens.Student.Pages.StudentSidebarContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.project.collabrix.R
import androidx.compose.foundation.Image
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentProfileScreen(navController: NavHostController) {
    val viewModel: StudentProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedPage by remember { mutableStateOf(StudentPage.Profile) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) { viewModel.loadProfile() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            StudentSidebarContent(
                selectedPage = selectedPage,
                onSelectPage = { page ->
                    if (page == StudentPage.Profile) {
                        selectedPage = StudentPage.Profile
                        scope.launch { drawerState.close() }
                    } else {
                        selectedPage = page

                        when (page) {
                            StudentPage.Dashboard -> navController.navigate("student_main")
                            StudentPage.BrowseResearch -> navController.navigate("student_main")
                            StudentPage.MyApplications -> navController.navigate("student_main")
                            else -> {}
                        }
                        
                        scope.launch { drawerState.close() }
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
                    when (uiState) {
                        is StudentProfileUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                        is StudentProfileUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text((uiState as StudentProfileUiState.Error).message, color = Color.Red)
                        }
                        is StudentProfileUiState.Deleted -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Account deleted.", color = Color.Red)
                        }
                        is StudentProfileUiState.Success -> {
                            val state = uiState as StudentProfileUiState.Success
                            var profile by remember { mutableStateOf(state.profile) }
                            val editMode = state.editMode
                            val scrollState = rememberScrollState()
                            var skillsText by remember { mutableStateOf(profile.skills.joinToString(", ")) }

                            LaunchedEffect(editMode) {
                                if (editMode) {
                                    skillsText = profile.skills.joinToString(", ")
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFF5F6FA))
                                    .verticalScroll(scrollState)
                            ) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Profile Settings",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 28.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                                )
                                Text(
                                    text = "Manage your personal information and account",
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
                                )
                                // Edit/Cancel and Save buttons
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, bottom = 8.dp),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = { viewModel.setEditMode(!editMode) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.height(36.dp)
                                    ) {
                                        Text(if (editMode) "Cancel" else "Edit Profile", color = Color.White, fontSize = 14.sp)
                                    }
                                    if (editMode) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Button(
                                            onClick = {
                                                val updatedProfile = profile.copy(
                                                    skills = skillsText.split(",").map { s -> s.trim() }.filter { s -> s.isNotEmpty() }
                                                )
                                                viewModel.saveProfile(updatedProfile)
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.height(36.dp)
                                        ) {
                                            Text("Save", color = Color.White, fontSize = 14.sp)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                // Profile Card
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    shadowElevation = 4.dp,
                                    color = Color.White,
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        // Avatar (black icon)
                                        Box(
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFF3F4F6)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.AccountCircle, contentDescription = "Avatar", modifier = Modifier.size(64.dp), tint = Color.Black)
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        if (editMode) {
                                            OutlinedTextField(
                                                value = profile.name ?: "",
                                                onValueChange = { profile = profile.copy(name = it) },
                                                label = { Text("Name") },
                                                modifier = Modifier.fillMaxWidth(),
                                                singleLine = true,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                        } else {
                                            Text(profile.name ?: "", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        // Role label (black background, white text)
                                        Box(
                                            modifier = Modifier
                                                .background(Color.Black, RoundedCornerShape(8.dp))
                                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                        ) {
                                            Text(profile.role ?: "Student", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        // Email (read-only)
                                        Text(profile.email ?: "", color = Color.Black)
                                        // Department
                                        if (editMode) {
                                            OutlinedTextField(
                                                value = profile.department ?: "",
                                                onValueChange = { profile = profile.copy(department = it) },
                                                label = { Text("Department") },
                                                modifier = Modifier.fillMaxWidth(),
                                                singleLine = true,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                        } else {
                                            Text(profile.department ?: "", color = Color.Black)
                                        }
                                    }
                                }
                                // About Me
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    shadowElevation = 4.dp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Column(
                                        Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            "About Me",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        if (editMode) {
                                            OutlinedTextField(
                                                value = profile.bio ?: "",
                                                onValueChange = { profile = profile.copy(bio = it) },
                                                label = { Text("About Me") },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                        } else {
                                            Text(
                                                profile.bio ?: "",
                                                color = Color.Black,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                                // Skills
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    shadowElevation = 4.dp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Column(
                                        Modifier.padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            "Skills",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        if (editMode) {
                                            OutlinedTextField(
                                                value = skillsText,
                                                onValueChange = { skillsText = it },
                                                label = { Text("Skills (comma separated)") },
                                                placeholder = { Text("e.g. Java, Python, Android") },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                        } else {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Column(
                                                    modifier = Modifier.fillMaxWidth(0.8f),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = spacedBy(8.dp)
                                                ) {
                                                    profile.skills.chunked(3).forEach { rowSkills ->
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.Center,
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            rowSkills.forEach { skill ->
                                                                Surface(
                                                                    shape = RoundedCornerShape(8.dp),
                                                                    color = Color.LightGray,
                                                                    shadowElevation = 2.dp,
                                                                    modifier = Modifier.padding(horizontal = 4.dp)
                                                                ) {
                                                                    Text(
                                                                        skill,
                                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
                                Spacer(modifier = Modifier.weight(1f))
                                // Danger Zone
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFFFEF2F2),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Column(Modifier.padding(16.dp)) {
                                        Text(
                                            "Danger Zone",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color.Red
                                        )
                                        Text(
                                            "Actions here cannot be undone. Please proceed with caution.",
                                            color = Color.Red.copy(alpha = 0.7f),
                                            fontSize = 14.sp,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Button(
                                            onClick = { showDeleteDialog = true },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Delete Account", color = Color.White)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                        is StudentProfileUiState.Saved -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Profile saved!", color = Color(0xFF22C55E))
                        }
                    }
                }
            }
        )
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAccount()
                        showDeleteDialog = false
                    }
                ) { Text("Yes, Delete", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    LaunchedEffect(uiState) {
        if (uiState is StudentProfileUiState.Deleted) {
            navController.navigate("landing") {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
} 
