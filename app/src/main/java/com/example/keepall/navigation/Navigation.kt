package com.example.keepall.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.keepall.screens.HomeScreen
import com.example.keepall.screens.camera.CameraScreen
import com.example.keepall.screens.notescreen.NoteScreen

@Composable
fun Navigation(startDestination: String = Screens.HomeScreen.route) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screens.HomeScreen.route) {
        HomeScreen(navController = navController)
        }
        composable(route = Screens.NoteScreen.route) {
            NoteScreen(navController)
        }
        composable(route = Screens.CameraScreen.route) {
            CameraScreen()
        }
    }
}