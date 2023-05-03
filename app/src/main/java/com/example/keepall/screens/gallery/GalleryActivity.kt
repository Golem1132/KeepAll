package com.example.keepall.screens.gallery

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.keepall.constants.PHOTOS_FOLDER
import com.example.keepall.constants.PHOTO_EXTENSION
import com.example.keepall.constants.PICKED_PHOTOS
import com.example.keepall.ui.theme.KeepAllTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.roundToInt

private const val GALLERY_PICKER_MODE = 1
private const val GALLERY_VIEWER_MODE = 0

@AndroidEntryPoint
class GalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[GalleryViewModel::class.java]
        lifecycleScope.launch(Dispatchers.Default) {
            fetchImages(this@GalleryActivity) { path ->
                viewModel.addFetchedImage(path)
            }
            intent.extras?.getStringArray(PICKED_PHOTOS)?.forEach {
                viewModel.putImageToCheckedList(it)
            }
        }
        setContent {
            KeepAllTheme {
                val imageSize =
                    (LocalConfiguration.current.screenWidthDp.times(LocalDensity.current.density)).roundToInt() / 2
                val fetchedImages = viewModel.images.collectAsState()
                val checkedImages = viewModel.imagesChecked.collectAsState()
                val galleryState = remember {
                    mutableStateOf(GALLERY_VIEWER_MODE)
                }
                Surface(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        topBar = {
                            GalleryTopBar(
                                clearItems = viewModel::clearCheckedImages,
                                onNavIconClick = {
                                    setResult(RESULT_CANCELED)
                                    finish()
                                }
                            )
                        },
                        bottomBar = {
                            GalleryBottomBar(
                                currentMode = galleryState.value,
                                onActionClick = {
                                    if (galleryState.value == GALLERY_PICKER_MODE)
                                        galleryState.value = GALLERY_VIEWER_MODE
                                    else galleryState.value = GALLERY_PICKER_MODE
                                },
                                onFabClick = {
                                    println(checkedImages.value)
                                    val intent = Intent().putExtra(
                                        PICKED_PHOTOS,
                                        checkedImages.value.toTypedArray()
                                    )
                                    setResult(RESULT_OK, intent)
                                    finish()
                                }
                            )
                        }
                    ) { paddingValues ->
                        LazyVerticalGrid(
                            contentPadding = paddingValues,
                            columns = GridCells.Fixed(2)
                        ) {
                            when (galleryState.value) {
                                GALLERY_PICKER_MODE -> {
                                    items(items = fetchedImages.value) { item ->
                                        CheckableImage(
                                            path = item,
                                            size = imageSize,
                                            padding = PaddingValues(5.dp),
                                            checked = checkedImages.value.contains(item)
                                        ) {
                                            viewModel.putImageToCheckedList(it)
                                        }
                                    }
                                }
                                GALLERY_VIEWER_MODE -> {
                                    items(items = checkedImages.value) { item ->
                                        SlowLoadingImage(
                                            path = item,
                                            size = imageSize,
                                            padding = PaddingValues(5.dp),
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryTopBar(
    clearItems: () -> Unit,
    onNavIconClick: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            Icon(imageVector = Icons.Default.ArrowBack,
                contentDescription = "Go Back",
                modifier = Modifier.clickable {
                    onNavIconClick()
                })
        },
        actions = {
            Icon(imageVector = Icons.Default.Refresh,
                contentDescription = "Clear checked items",
                modifier = Modifier.clickable {
                    clearItems()
                })
        }
    )
}

@Composable
fun SlowLoadingImage(
    path: String,
    size: Int,
    padding: PaddingValues
) {

    val img: MutableState<Bitmap?> = remember {
        mutableStateOf(null)
    }

    LaunchedEffect(key1 = true) {
        img.value = withContext(Dispatchers.Default) {
            try {
                Bitmap.createScaledBitmap(
                    BitmapFactory.decodeFile(path),
                    size, size, true
                )
            } catch (e: NullPointerException) {
                println(path)
                null
            }
        }
    }
    Box(
        modifier = Modifier
            .padding(padding),
        contentAlignment = Alignment.TopEnd
    ) {
        if (img.value != null) {
            Image(
                bitmap = img.value!!.asImageBitmap(),
                contentDescription = "Image from your gallery"
            )
        } else CircularProgressIndicator(
            modifier = Modifier.aspectRatio(1f)
        )
    }
}


@Composable
fun CheckableImage(
    path: String,
    size: Int,
    padding: PaddingValues,
    checked: Boolean,
    onClick: (String) -> Unit
) {

    val img: MutableState<Bitmap?> = remember {
        mutableStateOf(null)
    }
    val mChecked = remember {
        mutableStateOf(checked)
    }

    LaunchedEffect(key1 = true) {
        img.value = withContext(Dispatchers.Default) {
            try {
                Bitmap.createScaledBitmap(
                    BitmapFactory.decodeFile(path),
                    size, size, true
                )
            } catch (e: NullPointerException) {
                println(path)
                null
            }
        }
    }
    Box(
        modifier = Modifier
            .padding(padding)
            .clickable {
                onClick(path)
                mChecked.value = !mChecked.value
            },
        contentAlignment = Alignment.TopEnd
    ) {
        if (img.value != null) {
            Image(
                bitmap = img.value!!.asImageBitmap(),
                contentDescription = "Image from your gallery"
            )
            Checkbox(
                checked = mChecked.value,
                onCheckedChange = {
                    mChecked.value = !mChecked.value
                })
        } else CircularProgressIndicator(
            modifier = Modifier.aspectRatio(1f)
        )

    }
}


@Composable
fun GalleryBottomBar(
    currentMode: Int,
    onActionClick: () -> Unit,
    onFabClick: () -> Unit
) {
    BottomAppBar(
        actions = {
            Text(
                text = if (currentMode == GALLERY_PICKER_MODE)
                    "View selected"
                else "Pick images",
                modifier = Modifier.clickable {
                    onActionClick()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onFabClick()
                }) {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = "Add selected"
                )
            }
        }
    )
}


fun fetchImages(context: Context, doWhenFound: (String) -> Unit) {
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
    val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
    while (cursor.moveToNext()) {
        doWhenFound(cursor.getString(columnIndex))
    }
    cursor.close()
    File("${context.filesDir}/$PHOTOS_FOLDER").listFiles()?.forEach {
        if (it.extension == PHOTO_EXTENSION && it.length() > 0L) {
            doWhenFound(it.canonicalPath)
        }
    }
}
