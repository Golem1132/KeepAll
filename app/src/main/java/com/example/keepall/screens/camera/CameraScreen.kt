package com.example.keepall.screens.camera

import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.keepall.components.CameraBottomBar
import com.example.keepall.uievents.CameraUiState
import kotlinx.coroutines.launch
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
    Scaffold(
        bottomBar = {
            CameraBottomBar(
                imageCapture,
                mainExecutor,
                localContext,
                viewModel,
                cameraUiState.value
            )
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