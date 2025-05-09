package com.project.collabrix.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.collabrix.ui.screens.auth.ForgotPasswordScreen
import com.project.collabrix.ui.screens.auth.LandingScreen
import com.project.collabrix.ui.screens.auth.LoginScreen
import com.project.collabrix.ui.screens.auth.SignUpScreen
import com.project.collabrix.ui.screens.auth.UserRole
import com.project.collabrix.ui.screens.main.AdminMainScreen
import com.project.collabrix.ui.screens.main.ProfessorMainScreen
import andorid.example.collabrix.View.StudentUi.Pages.DashboardPage

sealed class AuthScreen(val route: String) {
    object Landing : AuthScreen("landing")
    object Login : AuthScreen("login")
    object SignUp : AuthScreen("signup")
    object ForgotPassword : AuthScreen("forgot_password")
    object StudentMain : AuthScreen("student_main")
    object ProfessorMain : AuthScreen("professor_main")
    object AdminMain : AuthScreen("admin_main")
}

@Composable
fun AuthNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AuthScreen.Landing.route
    ) {
        composable(AuthScreen.Landing.route) {
            LandingScreen(
                onGetStarted = {
                    navController.navigate(AuthScreen.Login.route)
                }
            )
        }
        
        composable(AuthScreen.Login.route) {
            LoginScreen(
                onLogin = { userRole ->
                    when (userRole) {
                        UserRole.STUDENT -> navController.navigate(AuthScreen.StudentMain.route) {
                            popUpTo(AuthScreen.Landing.route) { inclusive = true }
                        }
                        UserRole.PROFESSOR -> navController.navigate(AuthScreen.ProfessorMain.route) {
                            popUpTo(AuthScreen.Landing.route) { inclusive = true }
                        }
                        UserRole.ADMIN -> navController.navigate(AuthScreen.AdminMain.route) {
                            popUpTo(AuthScreen.Landing.route) { inclusive = true }
                        }
                    }
                },
                onSignUp = {
                    navController.navigate(AuthScreen.SignUp.route)
                },
                onForgotPassword = {
                    navController.navigate(AuthScreen.ForgotPassword.route)
                }
            )
        }
        
        composable(AuthScreen.SignUp.route) {
            SignUpScreen(
                onSignUp = { userRole ->
                    when (userRole) {
                        UserRole.STUDENT -> navController.navigate(AuthScreen.StudentMain.route) {
                            popUpTo(AuthScreen.Landing.route) { inclusive = true }
                        }
                        UserRole.PROFESSOR -> navController.navigate(AuthScreen.ProfessorMain.route) {
                            popUpTo(AuthScreen.Landing.route) { inclusive = true }
                        }
                        UserRole.ADMIN -> navController.navigate(AuthScreen.AdminMain.route) {
                            popUpTo(AuthScreen.Landing.route) { inclusive = true }
                        }
                    }
                },
                onLogin = {
                    navController.navigate(AuthScreen.Login.route)
                }
            )
        }

        composable(AuthScreen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onResetPassword = {
                    // Handle password reset logic here
                },
                onBackToLogin = {
                    navController.navigate(AuthScreen.Login.route)
                }
            )
        }

        composable(AuthScreen.StudentMain.route) {
            DashboardPage(
                navController = navController
            )
        }

        composable(AuthScreen.ProfessorMain.route) {
            ProfessorMainScreen(
                onLogout = {
                    navController.navigate(AuthScreen.Landing.route) {
                        popUpTo(AuthScreen.ProfessorMain.route) { inclusive = true }
                    }
                },
                navController = navController
            )
        }

        composable(AuthScreen.AdminMain.route) {
            AdminMainScreen(
                onLogout = {
                    navController.navigate(AuthScreen.Landing.route) {
                        popUpTo(AuthScreen.AdminMain.route) { inclusive = true }
                    }
                }
            )
        }
    }
} 