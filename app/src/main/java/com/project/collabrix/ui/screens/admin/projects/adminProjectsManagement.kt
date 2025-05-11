package com.collabrix

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Data class for project information
data class Project(
    val name: String = "",
    val professor: String = "",
    val department: String = "",
    val students: String = "",
    val status: String = ""
)

@Composable
fun ProjectManagementPage() {
    // State holders
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("") }
    var selectedDepartment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Section
        Text("Project Management")
        Text("Manage and monitor all research projects")

        // Search Bar Placeholder
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search Projects...") },
            modifier = Modifier.fillMaxWidth()
        )

        // Filter Section Placeholder
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Status Filter
            OutlinedButton(onClick = { /* TODO */ }) {
                Text("All Statuses")
            }

            // Department Filter
            OutlinedButton(onClick = { /* TODO */ }) {
                Text("All Departments")
            }
        }

        // Projects Table Placeholder
        LazyColumn {
            // Header Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Project Name")
                    Text("Professor")
                    Text("Department")
                    Text("Students")
                    Text("Status")
                    Text("Actions")
                }
            }

            // Sample Project Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Project 1")
                    Text("Dr. Smith")
                    Text("Computer Science")
                    Text("3/5")
                    Text("Active")
                    Button(onClick = { /* TODO */ }) {
                        Text("Suspend")
                    }
                }
            }
        }
    }
}

@Composable
fun Button(onClick: () -> Unit, content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}

@Composable
fun Text(x0: String) {
    TODO("Not yet implemented")
}

// Main entry point
fun main() {
    // TODO: Set up MaterialTheme and window
}