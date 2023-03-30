package com.example.keepall

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.keepall.ui.theme.KeepAllTheme
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CameraActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeepAllTheme {
                val scope = rememberCoroutineScope()
                val lifecycleOwner = LocalLifecycleOwner.current

                AndroidView(factory = { context ->
                    val previewView = PreviewView(context).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }

                    val cameraUseCase = Preview.Builder()
                        .build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                    scope.launch {
                        val cameraProvider = suspendCoroutine<ProcessCameraProvider> { continuation ->
                            ProcessCameraProvider.getInstance(context).also {
                                it.addListener({
                                    continuation.resume(it.get())
                                }, ContextCompat.getMainExecutor(context))
                            }
                        }
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, cameraUseCase)
                        } catch (e: Exception) {
                            Log.e("CAMERA", "${e.message}")
                        }

                    }
                    previewView
                })


            }
        }
    }
}

