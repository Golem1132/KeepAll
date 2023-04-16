package com.example.keepall.screens.notescreen

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.keepall.screens.camera.CameraActivity
import com.example.keepall.screens.canvas.CanvasActivity
import com.example.keepall.R
import com.example.keepall.components.Gallery
import com.example.keepall.constants.CANVAS_PATH
import com.example.keepall.constants.PHOTO_PATH
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(navController: NavController) {
    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = { it != SheetValue.PartiallyExpanded }
    )
    val viewModel = viewModel<NoteViewModel>()
    val files = viewModel.filesList.collectAsState()

    LaunchedEffect(key1 = true) {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val cursor = localContext.contentResolver.query(
            uri,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED
        )
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        var absolutePath: String
        while (cursor.moveToNext()) {
            absolutePath = cursor.getString(columnIndex)
            viewModel.fetchFile(File(absolutePath))
        }
        cursor.close()
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.photoFilePath = result.data?.getStringExtra(PHOTO_PATH)
                println(viewModel.photoFilePath)
            }
        }
    val canvasLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.canvasFilePath = result.data?.getStringExtra(CANVAS_PATH)
                println(viewModel.canvasFilePath)
            }
        }
    val textState = remember {
        mutableStateOf("")
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(bottomBar = {
            BottomAppBar(
                actions = {
                    Icon(painter = painterResource(id = R.drawable.baseline_palette_24),
                        contentDescription = "Add your painting",
                        modifier = Modifier
                            .clickable {
                                canvasLauncher.launch(
                                    Intent(
                                        localContext,
                                        CanvasActivity::class.java
                                    )
                                )
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp))
                    Icon(painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                        contentDescription = "Take photo",
                        modifier = Modifier
                            .clickable {
                                cameraLauncher.launch(
                                    Intent(
                                        localContext,
                                        CameraActivity::class.java
                                    )
                                )
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp))
                    Icon(painter = painterResource(id = R.drawable.baseline_add_to_photos_24),
                        contentDescription = "Pick photos",
                        modifier = Modifier
                            .clickable {
                                scope.launch {
                                    sheetState.show()
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp))
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
                    }
                }
            )
        },
            topBar = {
                Icon(imageVector = Icons.Default.Close,
                    contentDescription = "Discard note",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            navController.popBackStack()
                        })
            }) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                OutlinedTextField(
                    value = textState.value, onValueChange = { newText ->
                        textState.value = newText
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.9f)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
    }
    if (sheetState.isVisible)
        Gallery(files.value, sheetState, viewModel::addFile, viewModel.checkedPhotos)
    else Box {}
}