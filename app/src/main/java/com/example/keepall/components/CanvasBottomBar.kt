package com.example.keepall.components

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.keepall.screens.canvas.CanvasViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
fun CanvasBottomBar(
    onSave: () -> Unit,
    onExit: () -> Unit
) {

    Row(modifier = Modifier.fillMaxWidth()
    .background(color = MaterialTheme.colorScheme.secondaryContainer)
    .padding(horizontal = 16.dp, vertical = 12.dp),
    horizontalArrangement = Arrangement.SpaceAround) {
        Icon(imageVector = Icons.Default.Close, contentDescription = "",
            modifier = Modifier.size(48.dp)
                .clickable {
                    onExit()
                })
        Icon(imageVector = Icons.Default.Check, contentDescription = "",
            modifier = Modifier.size(48.dp)
                .clickable {
                    onSave()
                })
    }
}