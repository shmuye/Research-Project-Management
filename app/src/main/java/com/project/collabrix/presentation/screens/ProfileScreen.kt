package com.project.collabrix.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import com.project.collabrix.presentation.ProfileUiState
import com.project.collabrix.presentation.ProfileViewModel
import com.project.collabrix.data.dto.UserProfileUpdate
import androidx.navigation.NavController
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onAccountDeleted: () -> Unit = {},
    navController: NavController? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }

    val profile = (uiState as? ProfileUiState.Success)?.profile
    var name by remember(profile) { mutableStateOf(profile?.name ?: "") }
    var department by remember(profile) { mutableStateOf(profile?.department ?: "") }
    var bio by remember(profile) { mutableStateOf(profile?.bio ?: "") }
    var researchInterests by remember(profile) { mutableStateOf(profile?.getResearchInterestsList() ?: emptyList()) }
    var researchInterestsText by remember(profile) { mutableStateOf(profile?.getResearchInterestsList()?.joinToString(", ") ?: "") }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    // Update research interests when text changes
    LaunchedEffect(researchInterestsText) {
        if (editMode) {
            researchInterests = researchInterestsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        }
    }

    when (uiState) {
        is ProfileUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { 
            CircularProgressIndicator() 
        }
        is ProfileUiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text((uiState as ProfileUiState.Error).message, color = Color.Red)
        }
        is ProfileUiState.Deleted -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Account deleted.", color = Color.Red)
        }
        is ProfileUiState.Success -> {
            val scrollState = rememberScrollState()

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
                        onClick = { editMode = !editMode },
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
                                viewModel.saveProfile(
                                    UserProfileUpdate(
                                        name = name,
                                        department = department,
                                        bio = bio,
                                        researchInterests = researchInterests.joinToString(", ")
                                    )
                                )
                                editMode = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Save", color = Color.White, fontSize = 14.sp)
                        }
                    }
                }

                // Profile Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 4.dp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar
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
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Name") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp)
                            )
                        } else {
                            Text(name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        // Role label
                        Box(
                            modifier = Modifier
                                .background(Color.Black, RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(profile?.role ?: "PROFESSOR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(profile?.email ?: "", color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        if (editMode) {
                            OutlinedTextField(
                                value = department,
                                onValueChange = { department = it },
                                label = { Text("Department") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp)
                            )
                        } else {
                            Text(department, color = Color.Black)
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
                    Column(Modifier.padding(16.dp)) {
                        Text("About Me", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        if (editMode) {
                            OutlinedTextField(
                                value = bio,
                                onValueChange = { bio = it },
                                label = { Text("About Me") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                        } else {
                            Text(bio, color = Color.Black)
                        }
                    }
                }

                // Research Interests
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 4.dp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Research Interests", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        if (editMode) {
                            OutlinedTextField(
                                value = researchInterestsText,
                                onValueChange = { researchInterestsText = it },
                                label = { Text("Research Interests (comma separated)") },
                                placeholder = { Text("e.g. Machine Learning, Data Science, AI") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                researchInterests.forEach { interest ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.LightGray,
                                        shadowElevation = 2.dp
                                    ) {
                                        Text(interest, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // Danger Zone
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFEF2F2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Danger Zone", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Red)
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
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Delete Account", color = Color.White)
                        }
                    }
                }
            }
        }
        is ProfileUiState.Saved -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Profile saved!", color = Color(0xFF22C55E))
        }
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
                        onAccountDeleted()
                    }
                ) { Text("Yes, Delete", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }
} 