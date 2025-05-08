package com.project.collabrix.ui.theme.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AdminMainScreen(
    onLogout: () -> Unit,
    viewModel: AdminMainViewModel = hiltViewModel()
) {
    val backgroundColor = Color(0xFFF5F6FA)
    val primaryColor = Color(0xFF3B82F6)
    val textColor = Color(0xFF1F2937)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Admin Dashboard",
                    fontSize = 24.sp,
                    color = textColor
                )
                IconButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = primaryColor
                    )
                }
            }

            // Main Content
            Text(
                text = "Welcome to your Admin Dashboard!",
                fontSize = 18.sp,
                color = textColor,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Placeholder for future content
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
                        text = "System Overview",
                        fontSize = 20.sp,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No data available",
                        color = Color.Gray
                    )
                }
            }
        }
    }
} 