package com.project.collabrix.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.project.collabrix.data.dto.UserStatus

@Composable
fun UserSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Search users...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Gray
        ),
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
        selectedTabIndex = UserFilter.values().indexOf(selectedFilter),
        modifier = modifier.padding(horizontal = 16.dp),
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[UserFilter.values().indexOf(selectedFilter)]),
                height = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        UserFilter.values().forEach { filter ->
            Tab(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                text = {
                    Text(
                        text = when (filter) {
                            UserFilter.ALL_USERS -> "All Users"
                            UserFilter.STUDENTS -> "Students"
                            UserFilter.PROFESSORS -> "Professors"
                        },
                        fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Normal
                    )
                }
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
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp
    ) {
        Column {
            // Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F6FA))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableHeaderCell("Name", 3)
                TableHeaderCell("Email", 3)
                TableHeaderCell("Role", 2)
                TableHeaderCell("Department", 2)
                TableHeaderCell("Status", 1)
                TableHeaderCell("Actions", 1)
            }

            // Table Content
            users.forEach { user ->
                UserTableRow(user = user, onDelete = { onDeleteUser(user.id) })
            }
        }
    }
}

@Composable
private fun RowScope.TableHeaderCell(text: String, weight: Int) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .weight(weight.toFloat())
            .padding(horizontal = 4.dp)
    )
}

@Composable
private fun UserTableRow(
    user: UserManagementDto,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = user.name, modifier = Modifier.weight(3f))
        Text(text = user.email, modifier = Modifier.weight(3f))
        Text(text = user.role, modifier = Modifier.weight(2f))
        Text(text = user.department, modifier = Modifier.weight(2f))
        StatusChip(status = user.status, modifier = Modifier.weight(1f))
        IconButton(
            onClick = onDelete,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete user",
                tint = Color.Red
            )
        }
    }
}

@Composable
private fun StatusChip(
    status: UserStatus,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.padding(horizontal = 4.dp),
        shape = RoundedCornerShape(16.dp),
        color = when (status) {
            UserStatus.ACTIVE -> Color(0xFF4CAF50)
            UserStatus.INACTIVE -> Color(0xFFFF5252)
        }.copy(alpha = 0.1f)
    ) {
        Text(
            text = status.name,
            color = when (status) {
                UserStatus.ACTIVE -> Color(0xFF4CAF50)
                UserStatus.INACTIVE -> Color(0xFFFF5252)
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontWeight = FontWeight.Medium
        )
    }
} 