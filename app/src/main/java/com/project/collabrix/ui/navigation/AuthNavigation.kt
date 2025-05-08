package com.project.collabrix.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.collabrix.ui.theme.screens.ForgotPasswordScreen
import com.project.collabrix.ui.theme.screens.LandingScreen
import com.project.collabrix.ui.theme.screens.LoginScreen
import com.project.collabrix.ui.theme.screens.SignUpScreen

sealed class AuthScreen(val route: String) {
    object Landing : AuthScreen("landing")
    object Login : AuthScreen("login")
    object SignUp : AuthScreen("signup")
    object ForgotPassword : AuthScreen("forgot_password")
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
                onLogin = {
                    // Navigate to main app screen
                    navController.navigate("main") {
                        popUpTo(AuthScreen.Landing.route) { inclusive = true }
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
                onSignUp = {
                    // Navigate to main app screen
                    navController.navigate("main") {
                        popUpTo(AuthScreen.Landing.route) { inclusive = true }
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
    }
} 