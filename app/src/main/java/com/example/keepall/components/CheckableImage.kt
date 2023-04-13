package com.example.keepall.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun CheckableImage(
    path: String,
    addFile: (String) -> Unit,
    img: ImageBitmap,
    checked: Boolean
) {
    val _checked = remember {
        mutableStateOf(checked)
    }
    Box {
        Image(
            modifier = Modifier
                .padding(2.dp)
                .aspectRatio(1f)
                .clickable {
                    addFile(path)
                    _checked.value = !_checked.value
                },
            contentScale = ContentScale.FillBounds,
            bitmap = img,
            contentDescription = "Image from your device"
        )
        Checkbox(checked = _checked.value, onCheckedChange = {
            _checked.value = it
            addFile(path)
        })
    }
}