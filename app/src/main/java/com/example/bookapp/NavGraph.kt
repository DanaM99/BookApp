package com.example.bookapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.bookapp.screens.*
import com.google.gson.Gson
import java.net.URLDecoder
import com.example.bookapp.data.model.BookItem

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Main : Screen("main")
    object Search : Screen("search")
    object SavedBooks : Screen("savedBooks")

    object BookDetail : Screen("book_detail/{bookJson}") {
        fun createRoute(bookJson: String) = "book_detail/$bookJson"
    }

    object SavedBookDetail : Screen("savedBookDetail/{bookId}") {
        fun createRoute(bookId: String) = "savedBookDetail/$bookId"
    }
}

@Composable
fun NavGraph(startDestination: String = Screen.Login.route) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Login.route) {
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
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                onGoToSearch = {
                    navController.navigate(Screen.Search.route)
                },
                onGoToSavedBooks = {
                    navController.navigate(Screen.SavedBooks.route)
                }
            )
        }

        composable(Screen.Search.route) {
            SearchBooksScreen(navController = navController)
        }

        //  Mis libros guardados
        composable(Screen.SavedBooks.route) {
            SavedBooksScreen(onBookClick = { bookId ->
                navController.navigate(Screen.SavedBookDetail.createRoute(bookId))
            })
        }

        //  Detalle de libro guardado
        composable(
            route = Screen.SavedBookDetail.route,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
            SavedBookDetailScreen(
                bookId = bookId,
                onBack = { navController.popBackStack() }
            )
        }

        //  Detalle de libro desde búsqueda
        composable(
            route = Screen.BookDetail.route,
            arguments = listOf(navArgument("bookJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookJson = backStackEntry.arguments?.getString("bookJson") ?: ""
            val book = Gson().fromJson(URLDecoder.decode(bookJson, "UTF-8"), BookItem::class.java)
            BookDetailScreen(book = book)
        }
    }
}
