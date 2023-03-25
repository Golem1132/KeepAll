package com.example.keepall.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.keepall.components.AddNoteFAB
import com.example.keepall.navigationbar.KeepAllNavigationBar

@Composable
fun HomeScreen(navController: NavController) {
Scaffold(bottomBar = {
    KeepAllNavigationBar(navController = navController)
},
floatingActionButtonPosition = FabPosition.End,
floatingActionButton = {
    AddNoteFAB(navController = navController)
}) {
    LazyColumn(modifier = Modifier.padding(it)){

    }

}
}