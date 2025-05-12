package com.project.collabrix.ui.screens.admin.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.collabrix.data.dto.UserDto
import androidx.navigation.NavController
import com.project.collabrix.ui.screens.main.AdminPage

@Composable
fun AdminDashboardContent(
    viewModel: AdminDashboardViewModel = hiltViewModel(),
    onNavigateToUserManagement: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.fetchDashboardStats() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F6FA)
    ) {
        when (uiState) {
            is AdminDashboardUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is AdminDashboardUiState.Error -> {
                val message = (uiState as AdminDashboardUiState.Error).message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $message", color = Color.Red)
                }
            }
            is AdminDashboardUiState.Success -> {
                val stats = (uiState as AdminDashboardUiState.Success).stats
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    item {
                        Text(
                            text = "Admin Dashboard",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "System overview and management",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        StatCard(title = "Total Users", value = stats.totalUsers)
                        Spacer(modifier = Modifier.height(12.dp))
                        StatCard(title = "Active Projects", value = stats.activeProjects)
                        Spacer(modifier = Modifier.height(12.dp))
                        StatCard(title = "Professors", value = stats.totalProfessors)
                        Spacer(modifier = Modifier.height(12.dp))
                        StatCard(title = "Students", value = stats.totalStudents)
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Recent User Registrations",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        if (stats.recentUsers.isEmpty()) {
                            Text("No recent users found.", color = Color.Gray, fontSize = 16.sp)
                        }
                    }
                    if (stats.recentUsers.isNotEmpty()) {
                        items(stats.recentUsers) { user ->
                            RecentUserItem(user)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = onNavigateToUserManagement,
                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                                    border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("View All Users", color = Color.Black, fontWeight = FontWeight.Bold)
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
private fun StatCard(title: String, value: Any) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = ButtonDefaults.outlinedButtonBorder(enabled = true),
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
private fun RecentUserItem(user: UserDto) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = ButtonDefaults.outlinedButtonBorder(enabled = true),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            val initials = user.name?.split(" ")?.mapNotNull { it.firstOrNull()?.toString() }?.take(2)?.joinToString("") ?: "--"
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(initials, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(user.name ?: user.email, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = Color.Black)
                Text(
                    when (user.role.uppercase()) {
                        "STUDENT" -> "Student"
                        "PROFESSOR" -> "Professor"
                        "ADMIN" -> "Admin"
                        else -> user.role.replaceFirstChar { it.uppercase() }
                    },
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .background(Color.Black, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("NEW", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
} 