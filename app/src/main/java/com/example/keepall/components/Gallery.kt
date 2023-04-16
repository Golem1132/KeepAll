package com.example.keepall.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Gallery(
    files: List<File>,
    state: SheetState,
    addFile: (String) -> Unit,
    checkedPhotos: ArrayList<String>
) {
    val scope = rememberCoroutineScope()
    ModalBottomSheet(onDismissRequest = {
        scope.launch {
            state.hide()
        }
    }) {
        Box() {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(0.4f)
                    .padding(10.dp)
                    .zIndex(1f),
                columns = GridCells.Fixed(2)
            ) {
                items(
                    with(files) {
                        if (size > 1)
                            reversed()
                        else this
                    }
                ) {
                    val img =
                        BitmapFactory.decodeFile(it.canonicalPath, BitmapFactory.Options().apply {
                            inSampleSize = 4
                        })
                    CheckableImage(
                        path = it.canonicalPath,
                        addFile,
                        img = img.asImageBitmap(),
                        checkedPhotos.contains(it.canonicalPath)
                    )
                }
            }
        }
    }
}