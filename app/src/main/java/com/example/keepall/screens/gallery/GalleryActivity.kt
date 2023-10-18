package com.example.keepall.screens.gallery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.example.keepall.constants.PICKED_PHOTOS
import com.example.keepall.topbar.KeepAllTopAppBar
import com.example.keepall.ui.theme.KeepAllTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class GalleryActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[GalleryViewModel::class.java]
        lifecycleScope.launch(Dispatchers.Default) {
            fetchImages(this@GalleryActivity) { path ->
                viewModel.addFetchedImage(path)
            }
        }
        setContent {
            KeepAllTheme {
                val fetchedImages = viewModel.images.collectAsState()
                val checkedImages = viewModel.imagesChecked.collectAsState()
                var isPickingMultiple by remember {
                    mutableStateOf(false)
                }
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        topBar = {
                            KeepAllTopAppBar(
                                mainIcon = {
                                    Icon(
                                        modifier = Modifier.clickable {
                                            setResult(RESULT_CANCELED)
                                            finish()
                                        },
                                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                        contentDescription = "Go back"
                                    )
                                },
                                secondaryIcon = {
                                    Icon(
                                        modifier = Modifier.clickable {
                                            isPickingMultiple = false
                                            viewModel.clearCheckedImages()
                                        },
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Exit multipicking "
                                    )
                                },
                                actionIcon = {
                                    Text(
                                        modifier = Modifier.clickable {
                                            val intent = Intent().putExtra(
                                                PICKED_PHOTOS,
                                                checkedImages.value.toTypedArray()
                                            )
                                            setResult(RESULT_OK, intent)
                                            finish()
                                        },
                                        text = "Pick"
                                    )
                                },
                                title = {
                                    Text(text = "Picked ${checkedImages.value.size}")
                                },
                                isTitleVisible = isPickingMultiple
                            )
                        }
                    ) { paddingValues ->
                        LazyVerticalGrid(
                            contentPadding = paddingValues,
                            columns = GridCells.Fixed(2)
                        ) {
                            items(items = fetchedImages.value) { item ->
                                AsyncImage(
                                    modifier = Modifier
                                        .windowInsetsPadding(
                                            WindowInsets(
                                                left = 10.dp,
                                                top = 10.dp,
                                                right = 10.dp,
                                                bottom = 10.dp,
                                            )
                                        )
                                        .combinedClickable(
                                            onClick = {
                                                if (isPickingMultiple) {
                                                    viewModel.putImageToCheckedList(item)
                                                } else {
                                                    val intent = Intent().putExtra(
                                                        PICKED_PHOTOS,
                                                        listOf(item).toTypedArray()
                                                    )
                                                    setResult(RESULT_OK, intent)
                                                    finish()
                                                }
                                            },
                                            onLongClick = {
                                                if (!isPickingMultiple) {
                                                    viewModel.putImageToCheckedList(item)
                                                    isPickingMultiple = true
                                                }
                                            }
                                        ),
                                    model = File(item),
                                    contentDescription = "Photo in your device",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun fetchImages(context: Context, doWhenFound: (String) -> Unit) {
    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val projection =
        arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
    val cursor = context.contentResolver.query(
        uri,
        projection,
        null,
        null,
        MediaStore.Images.Media.DATE_ADDED
    )
    val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA) ?: return
    while (cursor.moveToNext()) {
        doWhenFound(cursor.getString(columnIndex))
    }
    cursor.close()
}
