package com.example.keepall.navigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.keepall.model.NavItem
import com.example.keepall.navigation.Screens

private val navItemsList = listOf(
    NavItem(
        Screens.HomeScreen.route,
        Icons.Default.List
    ),
    NavItem(
        "SearchScreen",
        Icons.Default.Search
    ),
    NavItem(
        "ImportantScreen",
        Icons.Default.Warning
    ),
    NavItem(
        "ShareScreen",
        Icons.Default.Share
    ),
)

@Composable
fun KeepAllNavigationBar(navController: NavController) {
    NavigationBar {
        navItemsList.forEach {
            NavigationBarItem(selected = it.route == navController.currentDestination?.route ,
                onClick = { /*TODO*/ },
                icon = {
                    Icon(imageVector = it.icon, contentDescription = "")
                })
        }

    }
}