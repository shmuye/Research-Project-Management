package com.project.collabrix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.collabrix.data.dto.UserFilter
import com.project.collabrix.data.dto.UserManagementDto

@Composable
fun UserSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search users...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        singleLine = true
    )
}

@Composable
fun UserFilterTabs(
    selectedFilter: UserFilter,
    onFilterSelected: (UserFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    TabRow(
        selectedTabIndex = selectedFilter.ordinal,
        modifier = modifier
    ) {
        UserFilter.values().forEach { filter ->
            Tab(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                text = { Text(filter.name.replace("_", " ")) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTable(
    users: List<UserManagementDto>,
    onDeleteUser: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Scroll hint
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Scroll to see more",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = "Scroll right",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Table
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
            ) {
                Column {
                    // Table Header
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(16.dp)
                            .width(900.dp), // Fixed width to ensure scrolling
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Name", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                        Text("Email", modifier = Modifier.weight(2.5f), fontWeight = FontWeight.Bold)
                        Text("Role", modifier = Modifier.weight(1.5f), fontWeight = FontWeight.Bold)
                        Text("Department", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold)
                        Text("Status", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                        Text("Actions", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                    }

                    // Table Content
                    users.forEach { user ->
                        Row(
                            modifier = Modifier
                                .width(900.dp) // Same fixed width as header
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(user.name ?: "", modifier = Modifier.weight(2f))
                            Text(user.email, modifier = Modifier.weight(2.5f))
                            Text(
                                user.role,
                                modifier = Modifier.weight(1.5f),
                                color = if (user.role == "PROFESSOR") Color(0xFF9C27B0) else Color(0xFF1976D2)
                            )
                            Text(user.department ?: "", modifier = Modifier.weight(2f))
                            Box(modifier = Modifier.weight(1f)) {
                                Surface(
                                    modifier = Modifier.padding(end = 8.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (user.isActive) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                                ) {
                                    Text(
                                        if (user.isActive) "Active" else "Inactive",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                        color = if (user.isActive) Color(0xFF4CAF50) else Color(0xFFF44336)
                                    )
                                }
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                IconButton(
                                    onClick = { onDeleteUser(user.id) }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete user",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                        if (users.last() != user) {
                            Divider(modifier = Modifier.width(900.dp))
                        }
                    }
                }
            }
        }
    }
} 