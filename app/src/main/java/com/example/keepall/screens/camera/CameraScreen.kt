package com.example.keepall.screens.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraEffect
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.keepall.R
import com.example.keepall.components.CameraBottomBar
import com.example.keepall.constants.PHOTO_PATH
import com.example.keepall.uievents.CameraUiState
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.roundToInt

@Composable
fun CameraScreen() {
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val localContext = LocalContext.current
    val mainExecutor = ContextCompat.getMainExecutor(localContext)
    val imageCapture = ImageCapture.Builder()
        .build()
    val viewModel = viewModel<CameraViewModel>()
    val cameraUiState = viewModel.cameraUiState.collectAsState()
    val type = remember {
        mutableStateOf(0)
    }
    //0 back
    //1 front
    Scaffold(
        bottomBar = {
            CameraBottomBar() {
                when (cameraUiState.value) {
                    is CameraUiState.Success -> {
                        Image(
                            modifier = Modifier
                                .size(72.dp)
                                .clickable {
                                    viewModel.goBackToPreview()
                                },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Reject photo"
                        )
                        Image(
                            modifier = Modifier
                                .size(72.dp)
                                .clickable {
                                    localContext as Activity
                                    val intent = Intent().also {
                                        it.putExtra(PHOTO_PATH, (cameraUiState.value as CameraUiState.Success).path)
                                    }
                                    localContext.setResult(Activity.RESULT_OK, intent)
                                    localContext.finish()
                                },
                            imageVector = Icons.Default.Check,
                            contentDescription = "Accept photo"
                        )
                    }
                    else -> {
                        Image(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(bottom = 0.dp)
                                .clickable {
                                    saveImage(imageCapture, mainExecutor, localContext, viewModel)
                                },
                            painter = painterResource(id = R.drawable.take_photo),
                            contentDescription = "Take photo"
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        AndroidView(factory = { context ->
            val previewView = PreviewView(context).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            val cameraPreview = Preview.Builder()
                .build().also {
                    it.targetRotation = previewView.rotation.roundToInt()
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
            scope.launch {
                val cameraProvider = suspendCoroutine<ProcessCameraProvider> { continuation ->
                    ProcessCameraProvider.getInstance(context).also {
                        it.addListener({
                            continuation.resume(it.get())
                        }, mainExecutor)
                    }
                }
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        cameraPreview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    Log.e("CAMERA", "${e.message}")
                }
            }
            previewView
        })
        when (cameraUiState.value) {
            is CameraUiState.Error -> {
                Toast.makeText(
                    localContext,
                    (cameraUiState.value as CameraUiState.Error).msg,
                    Toast.LENGTH_SHORT
                ).show()
            }
            is CameraUiState.Success -> {
                PreviewTakenPhoto(
                    padding = paddingValues,
                    bitmap = (cameraUiState.value as CameraUiState.Success).bitmap
                )
            }
            else -> {
                Box {}
            }
        }
    }

}

@Composable
fun PreviewTakenPhoto(padding: PaddingValues, bitmap: Bitmap) {
    Surface() {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Preview of the photo"
        )
    }
}

private fun saveImage(
    imageCapture: ImageCapture,
    executor: Executor,
    context: Context,
    viewModel: CameraViewModel
) {

    val imageFolder = File("${context.filesDir.absolutePath}/PHOTOS")
    if (!imageFolder.exists())
        imageFolder.mkdir()

    val outputFile = File(
        imageFolder,
        "${
            SimpleDateFormat(
                "yyyyMMddhhmmss",
                Locale.getDefault()
            ).format(System.currentTimeMillis())
        }.jpg"
    )
    outputFile.createNewFile()
    val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()
    imageCapture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            viewModel.sendPhoto(outputFile.canonicalPath)
        }

        override fun onError(exception: ImageCaptureException) {
            outputFile.delete()
            viewModel.sendError("Error while taking a photo")
        }

    })

}