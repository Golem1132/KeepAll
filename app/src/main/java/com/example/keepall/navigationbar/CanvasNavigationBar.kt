package com.example.keepall.navigationbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.keepall.model.CanvasItem

private val colorList = listOf(
    CanvasItem("Black", Color.Black),
    CanvasItem("Red", Color.Red),
    CanvasItem("Green", Color.Green),
    CanvasItem("Blue", Color.Blue),
    CanvasItem("Yellow", Color.Yellow),
)


@Composable
fun CanvasNavigationBar(
    selectedColor: Color,
    onClick: (Color) -> Unit
) {
    NavigationBar {
        colorList.forEach {
            NavigationBarItem(selected = it.color == selectedColor,
                onClick = { onClick(it.color) },
            icon = {
                   Box(modifier = Modifier.size(24.dp)
                       .background(color = it.color))
            },
            label = {
                Text(text = it.name)
            })
        }

    }
}