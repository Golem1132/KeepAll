package com.example.keepall.screens.notescreen

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keepall.screens.camera.CameraActivity
import com.example.keepall.screens.canvas.CanvasActivity
import com.example.keepall.R
import com.example.keepall.sidesheet.SideSheetValue
import com.example.keepall.components.IconWithBadge
import com.example.keepall.sidesheet.SideSheet
import com.example.keepall.constants.CANVAS_PATH
import com.example.keepall.constants.PHOTO_PATH
import com.example.keepall.constants.PICKED_PHOTOS
import com.example.keepall.screens.gallery.*
import com.example.keepall.sidesheet.rememberSideSheetState
import com.example.keepall.utils.getScreenWidth

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteScreen(navController: NavController) {
    val localContext = LocalContext.current
    val viewModel = hiltViewModel<NoteViewModel>()
    val pickedPhotos = viewModel.pickedPhotos.collectAsState()
    val painting = viewModel.canvasFilePath.collectAsState()
    val sideSheetState = rememberSideSheetState(initValue = SideSheetValue.CLOSED)
    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data?.getStringExtra(PHOTO_PATH) != null)
                    viewModel.addNewPhoto(result.data!!.getStringExtra(PHOTO_PATH)!!)
            }
        }
    val canvasLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.addPainting(result.data?.getStringExtra(CANVAS_PATH))
            }
        }

    val filePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data?.getStringArrayExtra(PICKED_PHOTOS) != null) {
                    viewModel.addNewPhoto(result.data!!.getStringArrayExtra(PICKED_PHOTOS)!!)
                }
            }
        }

    val filePermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val intent = Intent(
                    localContext,
                    GalleryActivity::class.java
                ).also {
                    it.putExtra(PICKED_PHOTOS, pickedPhotos.value)
                }
                filePickerLauncher.launch(intent)
            }
        }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                cameraLauncher.launch(
                    Intent(
                        localContext,
                        CameraActivity::class.java
                    )
                )
            }
        }
    val textState = remember {
        mutableStateOf("")
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(bottomBar = {
            BottomAppBar(
                actions = {
                    IconWithBadge(
                        icon = painterResource(id = R.drawable.baseline_palette_24),
                        contentDescription = "Add your painting",
                        modifier = Modifier
                            .combinedClickable(onClick = {
                                canvasLauncher.launch(
                                    Intent(
                                        localContext,
                                        CanvasActivity::class.java
                                    )
                                )
                            },
                                onLongClick = {
                                    println("SHOW PAINTING")
                                }),
                        count = if (painting.value.isNotBlank())
                            1
                        else 0
                    )
                    IconWithBadge(
                        icon = painterResource(id = R.drawable.baseline_photo_camera_24),
                        contentDescription = "Take photo",
                        modifier = Modifier
                            .clickable {
                                cameraPermissionLauncher.launch(
                                    Manifest.permission.CAMERA
                                )
                            }, count = 0
                    )
                    IconWithBadge(icon = painterResource(id = R.drawable.baseline_add_to_photos_24),
                        contentDescription = "Pick photos",
                        modifier = Modifier
                            .combinedClickable(
                                onClick = {
                                    if (Build.VERSION.SDK_INT > 32)
                                        filePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                                    else
                                        filePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                },
                                onLongClick = {
                                    sideSheetState.open()
                                }
                            ), count = pickedPhotos.value.size
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        viewModel.addNewNote(textState.value)
                        navController.popBackStack()
                    }) {
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
        SideSheet(sideSheetState) {
            LazyColumn {
                items(items = pickedPhotos.value) { item ->
                    SlowLoadingImage(
                        path = item,
                        size = localContext.getScreenWidth(),
                        padding = PaddingValues(5.dp),
                    )
                }
            }
        }
    }
}