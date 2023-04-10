package com.example.keepall.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.keepall.R
import com.example.keepall.constants.PHOTO_PATH
import com.example.keepall.screens.camera.CameraViewModel
import com.example.keepall.uievents.CameraUiState
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

@Composable
fun CameraBottomBar(
    imageCapture: ImageCapture,
    executor: Executor,
    context: Context,
    viewModel: CameraViewModel,
    state: CameraUiState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        when (state) {
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
                            context as Activity
                            val intent = Intent().also {
                                it.putExtra(PHOTO_PATH, state.path)
                            }
                            context.setResult(Activity.RESULT_OK, intent)
                            context.finish()
                        },
                    imageVector = Icons.Default.Check,
                    contentDescription = "Accept photo"
                )
            }
            else -> {
                Image(
                    modifier = Modifier
                        .size(72.dp)
                        .clickable {
                            saveImage(imageCapture, executor, context, viewModel)
                        },
                    painter = painterResource(id = R.drawable.take_photo),
                    contentDescription = "Take photo"
                )
            }
        }

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