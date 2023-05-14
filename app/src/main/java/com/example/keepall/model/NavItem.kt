package com.example.keepall.model

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val route: String,
    val icon: ImageVector,
    val action: NavItemEvent
)

enum class NavItemEvent {
    DELETE, EXIT, NO_ACTION
}
