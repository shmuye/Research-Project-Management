package com.project.collabrix.ui.screens.main

import androidx.compose.runtime.Composable
import com.project.collabrix.ui.screens.professor.dashboard.ProfessorDashboardScreen

@Composable
fun ProfessorMainScreen(
    onLogout: () -> Unit
) {
    ProfessorDashboardScreen(
        onLogout = onLogout
    )
} 