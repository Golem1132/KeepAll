package com.example.keepall.screens.camera

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import com.example.keepall.R
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraScreen() {
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val localContext = LocalContext.current
    val mainExecutor = ContextCompat.getMainExecutor(localContext)
    val imageCapture = ImageCapture.Builder()
        .build()

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
                    lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, cameraPreview, imageCapture
                )
            } catch (e: Exception) {
                Log.e("CAMERA", "${e.message}")
            }
        }
        previewView
    })
    ImageCaptureLayout(imageCapture, mainExecutor, localContext)
}

@Composable
fun ImageCaptureLayout(imageCapture: ImageCapture, executor: Executor, context: Context) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Image(
            modifier = Modifier
                .size(72.dp)
                .clickable {
                    saveImage(imageCapture, executor, context)
                },
            painter = painterResource(id = R.drawable.take_photo),
            contentDescription = "Take photo"
        )
    }
}

fun saveImage(imageCapture: ImageCapture, executor: Executor, context: Context) {

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

        }

        override fun onError(exception: ImageCaptureException) {
            println("ERROR ${exception.message}")
        }

    })

}