package com.example.keepall.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.keepall.navigation.Screens

@Composable
fun AddNoteFAB(navController: NavController) {
    FloatingActionButton(onClick = {
        navController.navigate(route = Screens.NoteScreen.route)
    }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
    }
}