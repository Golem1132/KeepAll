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
import androidx.navigation.NavController
import com.example.keepall.screens.CanvasViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@Composable
fun CanvasBottomBar(
    scope: CoroutineScope,
    viewModel: CanvasViewModel,
    screenHeight: Int,
    screenWidth: Int,
    ctx: Context,
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
                    scope.launch {
                        val bitmap = viewModel.saveCanvasToJpg(screenWidth,screenHeight)
                        val dir = File("${ctx.filesDir.absolutePath}/CANVAS")
                        if(!dir.exists())
                            dir.mkdir()
                        val file = File(dir.absolutePath, "testCanva")
                        file.createNewFile()
                        val fos = FileOutputStream(file)
                        fos.use {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,it)
                        }
                        fos.flush()
                        fos.close()
                    }.invokeOnCompletion {
                        onExit()
                    }
                })
    }
}