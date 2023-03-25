package com.example.keepall.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.keepall.screens.CanvasScreen
import com.example.keepall.screens.HomeScreen
import com.example.keepall.screens.NoteScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.HomeScreen.route) {
        composable(route = Screens.HomeScreen.route) {
        HomeScreen(navController = navController)
        }
        composable(route = Screens.NoteScreen.route) {
            CanvasScreen()
        }
    }
}