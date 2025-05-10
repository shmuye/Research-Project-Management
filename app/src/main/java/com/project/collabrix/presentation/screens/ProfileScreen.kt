package com.project.collabrix.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.collabrix.R
import com.project.collabrix.presentation.ProfileUiState
import com.project.collabrix.presentation.ProfileViewModel
import com.project.collabrix.data.dto.UserProfileUpdate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onAccountDeleted: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Top Bar (only one hamburger menu)
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("manage your personal information and account", fontSize = 14.sp, color = Color.Black, modifier = Modifier.padding(bottom = 4.dp))
                Button(
                    onClick = { editMode = !editMode },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(if (editMode) "Cancel" else "Edit Profile", color = Color.White, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            // Profile Card
            item {
                when (uiState) {
                    is ProfileUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is ProfileUiState.Error -> {
                        Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                            Text(
                                text = (uiState as ProfileUiState.Error).message,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    is ProfileUiState.Deleted -> {
                        onAccountDeleted()
                    }
                    is ProfileUiState.Success -> {
                        val profile = (uiState as ProfileUiState.Success).profile
                        var name by remember { mutableStateOf(profile.name ?: "") }
                        var department by remember { mutableStateOf(profile.department ?: "") }
                        var bio by remember { mutableStateOf(profile.bio ?: "") }
                        val email = profile.email
                        val role = profile.role
                        var researchInterests by remember { mutableStateOf(profile.getResearchInterestsList()) }
                        var researchInterestsText by remember { mutableStateOf(researchInterests.joinToString(", ")) }
                        // Profile Card
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF3F4F6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AccountCircle, contentDescription = "Avatar", modifier = Modifier.size(64.dp), tint = Color(0xFF9CA3AF))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            if (editMode) {
                                OutlinedTextField(
                                    value = name,
                                    onValueChange = { name = it },
                                    label = { Text("Full Name") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            } else {
                                Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Text("Professor of Computer Science", fontSize = 15.sp, color = Color.Black)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .background(Color.Black, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(role, color = Color.White, fontSize = 13.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(email, fontSize = 14.sp, color = Color.Black)
                            Text("University of Technology", fontSize = 14.sp, color = Color.Black)
                            if (editMode) {
                                OutlinedTextField(
                                    value = department,
                                    onValueChange = { department = it },
                                    label = { Text("Department") },
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            } else {
                                Text("Department of  $department", fontSize = 14.sp, color = Color.Black)
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        // About Me
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .padding(16.dp)
                        ) {
                            Text("About Me", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                            if (editMode) {
                                OutlinedTextField(
                                    value = bio,
                                    onValueChange = { bio = it },
                                    label = { Text("Bio") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    minLines = 3,
                                    maxLines = 5
                                )
                            } else {
                                Text(bio, fontSize = 14.sp, color = Color.Black)
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        // Research Interests
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .padding(16.dp)
                        ) {
                            Text("Research Interests", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                            Spacer(modifier = Modifier.height(8.dp))
                            if (editMode) {
                                OutlinedTextField(
                                    value = researchInterestsText,
                                    onValueChange = {
                                        researchInterestsText = it
                                        researchInterests = it.split(",").map { s -> s.trim() }.filter { s -> s.isNotEmpty() }
                                    },
                                    label = { Text("Research Interests (comma separated)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            } else {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    researchInterests.forEach { interest ->
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFFF3F4F6), RoundedCornerShape(20.dp))
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                        ) {
                                            Text(interest, color = Color.Black, fontSize = 14.sp)
                                        }
                                    }
                                }
                            }
                        }
                        if (editMode) {
                            Spacer(modifier = Modifier.height(24.dp))
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
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
                            ) {
                                Text("Save Changes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                    else -> {}
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            // Danger Zone
            item {
                if (uiState is ProfileUiState.Success) {
                    var showDeleteDialog by remember { mutableStateOf(false) }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFFF1F2), RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Text("Danger Zone", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                        Text("Actions  here cannot be undone", color = Color(0xFFDC2626), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { showDeleteDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Delete Account", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    if (showDeleteDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteDialog = false },
                            title = { Text("Delete Account") },
                            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showDeleteDialog = false
                                        (viewModel as ProfileViewModel).deleteAccount()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                                ) {
                                    Text("Delete", color = Color.White)
                                }
                            },
                            dismissButton = {
                                OutlinedButton(onClick = { showDeleteDialog = false }) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
} 