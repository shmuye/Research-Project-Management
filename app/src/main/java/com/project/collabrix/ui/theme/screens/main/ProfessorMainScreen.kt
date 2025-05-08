package com.project.collabrix.ui.theme.screens.main

import androidx.compose.runtime.Composable
import com.project.collabrix.ui.theme.screens.main.professor.dashboard.ProfessorDashboardScreen

@Composable
fun ProfessorMainScreen(
    onLogout: () -> Unit
) {
    ProfessorDashboardScreen(
        onLogout = onLogout
    )
} 