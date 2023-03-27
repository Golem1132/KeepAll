package com.example.keepall.screens

import android.graphics.Bitmap
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.keepall.model.Line
import com.example.keepall.navigationbar.CanvasNavigationBar
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun CanvasScreen() {
    val viewModel = viewModel<CanvasViewModel>()
    val scope = rememberCoroutineScope()

    var screenHeight = LocalConfiguration.current.screenHeightDp / LocalConfiguration.current.densityDpi
    var screenWidth = LocalConfiguration.current.screenWidthDp / LocalConfiguration.current.densityDpi

    val ctx = LocalContext.current

    var selectedColor by remember {
        mutableStateOf(Color.Black)
    }

    var lastTouchX by remember {
        mutableStateOf(0f)
    }
    var lastTouchY by remember {
        mutableStateOf(0f)
    }

    val xd = viewModel.canvasStateFlow.collectAsState()
    val tempPath = remember {
        mutableStateOf<Line?>(Line())
    }
    var path = Line()
    Scaffold(topBar = {
        CanvasNavigationBar(selectedColor) {
            selectedColor = it
        }
    },
    floatingActionButton = {
        FloatingActionButton(onClick = {
            scope.launch {
                val bitmap = viewModel.saveCanvasToJpg(screenWidth,screenHeight)
                val dir = File("${ctx.filesDir.absolutePath}/CANVAS")
                if(!dir.exists())
                    dir.mkdir()
                val file = File(dir.absolutePath, "testCanva")
                file.createNewFile()
                val fos = FileOutputStream(file)
                fos.use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100,it)
                }
                fos.flush()
                fos.close()
            }.invokeOnCompletion {
                //TODO Exit from canvas go to note
                println("finished")
            }
        }) {

        }
    }) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        path.path.moveTo(event.x, event.y)
                        path.color = selectedColor
                        tempPath.value?.path?.moveTo(event.x, event.y)
                        tempPath.value?.color = selectedColor
                        lastTouchX = event.x
                        lastTouchY = event.y
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val historySize = event.historySize
                        for (i in 0 until historySize) {
                            val historyX = event.getHistoricalX(i)
                            val historyY = event.getHistoricalY(i)
                            path.path.lineTo(historyX, historyY)
                            tempPath.value?.path?.lineTo(historyX, historyY)
                        }
                        path.path.lineTo(event.x, event.y)
                        tempPath.value?.path?.lineTo(event.x, event.y)
                        lastTouchX = event.x
                        lastTouchY = event.y
                    }
                    MotionEvent.ACTION_UP -> {
                        viewModel.addNewLine(
                            Line(
                                path.path,
                                path.color
                            )
                        )
                        path = Line()
                        tempPath.value = Line()
                    }
                }

                lastTouchX = event.x
                lastTouchY = event.y
                val savePath = tempPath.value
                tempPath.value = null
                tempPath.value = savePath
                true
            }
            .onSizeChanged { size ->
                screenWidth = size.width
                screenHeight = size.height
            }) {
            xd.value.forEach { line ->
                drawPath(
                    path = line.path,
                    color = line.color,
                    style = Stroke(
                        width = 4.dp.toPx()
                    )
                )
            }
            tempPath.let { path ->
                drawPath(
                    path = path.value?.path ?: Path(),
                    color = path.value?.color ?: Color.Black,
                    style = Stroke(
                        width = 4.dp.toPx()
                    )
                )
            }

        }
    }
}