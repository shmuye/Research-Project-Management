package com.project.collabrix.ui.screens.main

import androidx.compose.runtime.Composable
import com.project.collabrix.ui.screens.professor.dashboard.ProfessorDashboardScreen
import androidx.navigation.NavHostController

@Composable
fun ProfessorMainScreen(
    onLogout: () -> Unit,
    navController: NavHostController
) {
    ProfessorDashboardScreen(
        onLogout = onLogout,
        navController = navController
    )
} 