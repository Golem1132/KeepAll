package com.example.keepall.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconWithBadge(modifier: Modifier, count: Int, icon: Painter, contentDescription: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .then(
                modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        BadgedBox(badge = {
            if (count > 0)
                Badge {
                    Text("$count")
                }
        }) {
            Icon(
                painter = icon,
                contentDescription = contentDescription
            )
        }
    }
}