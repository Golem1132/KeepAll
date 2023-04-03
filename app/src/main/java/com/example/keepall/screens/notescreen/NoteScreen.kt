package com.example.keepall.screens.notescreen

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
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
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.keepall.CameraActivity
import com.example.keepall.CanvasActivity
import com.example.keepall.R
import com.example.keepall.components.Gallery
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun NoteScreen(navController: NavController) {
    val localContext = LocalContext.current
    val viewModel = viewModel<NoteViewModel>()
    val numbers = viewModel.filesList.collectAsState()

        LaunchedEffect(key1 = true) {
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection =
                arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val cursor = localContext.contentResolver.query(
                uri,
                projection,
                null,
                null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER
            )
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            var absolutePath = ""
            while (cursor.moveToNext()) {
                absolutePath = cursor.getString(columnIndex)
                viewModel.add(File(absolutePath))
            }
            cursor.close()
        }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {

            }
        }
    val textState = remember {
        mutableStateOf("")
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(bottomBar = {
            NoteScreenBottomBar(launcher)
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
                TextField(
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
    Gallery(numbers.value)
}

@Composable
fun NoteScreenBottomBar(
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val localContext = LocalContext.current
    BottomAppBar(
        actions = {
            Icon(painter = painterResource(id = R.drawable.baseline_palette_24),
                contentDescription = "Add your painting",
                modifier = Modifier
                    .clickable {
                        launcher.launch(Intent(localContext, CanvasActivity::class.java))
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp))
            Icon(painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                contentDescription = "Take photo",
                modifier = Modifier
                    .clickable {
                        launcher.launch(Intent(localContext, CameraActivity::class.java))
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp))
            Icon(painter = painterResource(id = R.drawable.baseline_add_to_photos_24),
                contentDescription = "Pick photos",
                modifier = Modifier
                    .clickable {
//                    launcher.launch(Intent(localContext, CanvasActivity::class.java))
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp))
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        }
    )
}