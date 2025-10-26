package com.example.bookapp

import androidx.compose.runtime.Composable // <-- Faltaba
import androidx.navigation.compose.rememberNavController // <-- Faltaba
import androidx.navigation.compose.NavHost // <-- Faltaba
import androidx.navigation.compose.composable // <-- Faltaba
import com.example.bookapp.screens.LoginScreen // <-- Faltaban las referencias a las pantallas
import com.example.bookapp.screens.RegisterScreen
import com.example.bookapp.screens.MainScreen

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object Register: Screen("register")
    object Main: Screen("main")
}

@Composable
fun NavGraph(startDestination: String = Screen.Login.route) {
    // Estas referencias ahora se resuelven
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Login.route) {
            // PROBLEMA: LoginScreen y RegisterScreen no aceptan 'navController' directamente
            // Deben aceptar funciones lambda para la navegación, como ya las tenemos.
            // La navegación la haremos dentro del lambda 'onLoginSuccess'.

            // Si NavGraph.kt reemplaza su BookAppNavigation, DEBE usar las lambdas:
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                onLogout = {
                    // Asumiendo que la función de logout está en MainScreen o se llama desde allí
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
