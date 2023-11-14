package com.example.keepall.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun CheckableImage(
    checked: Boolean,
    isCheckboxVisible: Boolean,
    content: @Composable () -> Unit
) {
    Box(contentAlignment = Alignment.TopEnd) {
        content()
        if (isCheckboxVisible)
            Checkbox(checked = checked, onCheckedChange = {})
    }

}