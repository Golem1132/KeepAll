package com.example.keepall.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.keepall.screens.homescreen.HomeScreen
import com.example.keepall.screens.camera.CameraScreen
import com.example.keepall.screens.homescreen.HomeViewModel
import com.example.keepall.screens.notescreen.NoteScreen

@Composable
fun Navigation(startDestination: String = Screens.HomeScreen.route) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screens.HomeScreen.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
        HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = "${Screens.NoteScreen.route}?id={id}", arguments =
        listOf(
            navArgument("id") {
                type = NavType.IntType
                defaultValue = -1
            }
        )
        ) {
            NoteScreen(navController, it.arguments?.getInt("id"))
        }
        composable(route = Screens.CameraScreen.route) {
            CameraScreen()
        }
    }
}