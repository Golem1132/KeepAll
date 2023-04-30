package com.example.keepall.screens.canvas

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.keepall.components.CanvasBottomBar
import com.example.keepall.constants.CANVAS_PATH
import com.example.keepall.model.Line
import com.example.keepall.navigationbar.CanvasColorsRail
import com.example.keepall.ui.theme.KeepAllTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CanvasActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeepAllTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = viewModel<CanvasViewModel>()
                    val scope = rememberCoroutineScope()
                    val isColorRailOpened = remember {
                        mutableStateOf(false)
                    }
                    val isThicknessBarOpened = remember {
                        mutableStateOf(false)
                    }
                    val thickness = remember {
                        mutableStateOf(0f)
                    }

                    var screenHeight = 0
                    var screenWidth = 0

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

                    val canvasBacklog = viewModel.canvasStateFlow.collectAsState()
                    val tempPath = remember {
                        mutableStateOf<Line?>(Line())
                    }
                    var path = Line()
                    Scaffold(
                        containerColor = Color.White,
                        topBar = {
                            if (isColorRailOpened.value)
                                CanvasColorsRail(selectedColor) {
                                    selectedColor = it
                                }
                            if (isThicknessBarOpened.value)
                                Slider(modifier = Modifier.padding(horizontal = 20.dp),
                                    valueRange = 1f.rangeTo(50f),
                                    steps = 50,
                                    value = thickness.value, onValueChange = {
                                        thickness.value = it
                                    })
                        },
                        bottomBar = {
                            CanvasBottomBar(
                                onSave = {
                                    val job = scope.async {
                                        val bitmap =
                                            viewModel.saveCanvasToJpg(screenWidth, screenHeight)
                                        val dir = File("${ctx.filesDir.absolutePath}/CANVAS")
                                        if (!dir.exists())
                                            dir.mkdir()
                                        val date = SimpleDateFormat(
                                            "yyyyMMddHHmmss",
                                            Locale.getDefault()
                                        ).format(Date())
                                        val file = File(dir.absolutePath, "${date}_canvas.jpeg")
                                        file.createNewFile()
                                        val fos = FileOutputStream(file)
                                        fos.use {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                                        }
                                        fos.flush()
                                        fos.close()
                                        file.canonicalPath
                                    }
                                    job.invokeOnCompletion {
                                        val intent = Intent().apply {
                                            putExtra(CANVAS_PATH, job.getCompleted())
                                        }
                                        setResult(RESULT_OK, intent)
                                        finish()
                                    }
                                },
                                onExit = {
                                    setResult(RESULT_CANCELED)
                                    finish()
                                },
                                onThicknessClicked = {
                                    isColorRailOpened.value = false
                                    isThicknessBarOpened.value = !isThicknessBarOpened.value
                                },
                                onColorsClicked = {
                                    isColorRailOpened.value = !isColorRailOpened.value
                                    isThicknessBarOpened.value = false
                                }
                            )

                        }) {
                        Canvas(modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                PaddingValues(
                                    bottom = it.calculateBottomPadding()
                                )
                            )
                            .onSizeChanged { canvas ->
                                screenHeight = canvas.height
                                screenWidth = canvas.width
                            }
                            .pointerInteropFilter { event ->
                                when (event.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        path.path.moveTo(event.x, event.y)
                                        path.color = selectedColor
                                        path.thickness = thickness.value
                                        tempPath.value?.path?.moveTo(event.x, event.y)
                                        tempPath.value?.color = selectedColor
                                        tempPath.value?.thickness = thickness.value
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
                                                path.color,
                                                path.thickness
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
                            }) {
                            canvasBacklog.value.forEach { line ->
                                drawPath(
                                    path = line.path,
                                    color = line.color,
                                    style = Stroke(
                                        width = line.thickness.dp.toPx()
                                    )
                                )
                            }
                            tempPath.let { path ->
                                drawPath(
                                    path = path.value?.path ?: Path(),
                                    color = path.value?.color ?: Color.Black,
                                    style = Stroke(
                                        width = path.value?.thickness?.dp?.toPx() ?: 1.dp.toPx()
                                    )
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}