package com.example.keepall.screens.canvas

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.example.keepall.model.Line
import com.example.keepall.navigationbar.CanvasNavigationBar
import com.example.keepall.ui.theme.KeepAllTheme
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CanvasActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
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
                        CanvasNavigationBar(selectedColor) {
                            selectedColor = it
                        }
                    },
                        bottomBar = {
                            CanvasBottomBar(
                                onSave = {
                                    scope.launch {
                                        val bitmap = viewModel.saveCanvasToJpg(screenWidth,screenHeight)
                                        val dir = File("${ctx.filesDir.absolutePath}/CANVAS")
                                        if(!dir.exists())
                                            dir.mkdir()
                                        val date = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
                                        val file = File(dir.absolutePath, "$date\\_canvas.jpeg")
                                        file.createNewFile()
                                        val fos = FileOutputStream(file)
                                        fos.use {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,it)
                                        }
                                        fos.flush()
                                        fos.close()
                                    }.invokeOnCompletion {
                                        setResult(RESULT_OK)
                                        finish()
                                    }
                                }
                            ){
                                setResult(RESULT_CANCELED)
                                finish()
                            }

                        }) {
                        Canvas(modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                            .onSizeChanged { canvas ->
                                println("${canvas.height}, ${canvas.width}")
                                screenHeight = canvas.height
                                screenWidth = canvas.width
                            }
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
                            }) {
                            canvasBacklog.value.forEach { line ->
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
            }
        }
    }
}

