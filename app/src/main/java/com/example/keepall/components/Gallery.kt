package com.example.keepall.components

import android.graphics.BitmapFactory
import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import androidx.core.net.toFile
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun Gallery(files: List<File>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(0.4f)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(topEndPercent = 10, topStartPercent = 10)
                )
                .padding(10.dp),
            columns = GridCells.Fixed(2)
        ) {
            items(files) {
                val img = BitmapFactory.decodeFile(it.canonicalPath)
                Image(bitmap = img.asImageBitmap(), contentDescription = "")
            }

        }
    }
}