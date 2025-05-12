package com.project.collabrix.ui.screens.professor.applications

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReviewApplicationsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Review Applications",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Manage student applications for your research projects",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Placeholder cards
        ApplicationCard(name = "Name Placeholder", details = "Details Placeholder", status = "Status Placeholder")
        Spacer(modifier = Modifier.height(16.dp))
        ApplicationCard(name = "Name Placeholder", details = "Details Placeholder", status = "Status Placeholder")
    }
}

@Composable
fun ApplicationCard(name: String, details: String, status: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = name, fontSize = 18.sp)
            Text(text = details, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = { /* TODO: Decline action */ }) {
                    Text(text = "Decline")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /* TODO: Approve action */ }) {
                    Text(text = "Approve")
                }
            }
        }
    }
}