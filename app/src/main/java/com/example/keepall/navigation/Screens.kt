package com.example.keepall.navigation

sealed class Screens(val route: String) {
    object HomeScreen: Screens("HomeScreen")
    object NoteScreen: Screens("NoteScreen")

    object CameraScreen: Screens("CameraScreen")
}