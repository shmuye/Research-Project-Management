package com.example.collabrix.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AdminDashboardPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top App Bar Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Title Placeholder
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(24.dp)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .width(250.dp)
                .height(16.dp)
                .background(Color.Gray.copy(alpha = 0.3f))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dashboard Cards Placeholders
        repeat(4) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(vertical = 4.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section Title
        Box(
            modifier = Modifier
                .width(220.dp)
                .height(20.dp)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Placeholder for recent users
        repeat(3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .height(16.dp)
                            .background(Color.LightGray)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(160.dp)
                            .height(12.dp)
                            .background(Color.Gray.copy(alpha = 0.3f))
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(16.dp)
                        .background(Color.Black, shape = RoundedCornerShape(4.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // View All Users Button Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
        )
    }
}
