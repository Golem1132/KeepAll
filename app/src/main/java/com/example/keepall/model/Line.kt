package com.example.keepall.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path

data class Line(
    val path: Path = Path(),
    var color: Color = Color.Black
)
