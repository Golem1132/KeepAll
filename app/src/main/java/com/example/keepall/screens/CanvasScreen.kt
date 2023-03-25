package com.example.keepall.screens

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keepall.model.Line
import com.example.keepall.navigationbar.CanvasNavigationBar

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun CanvasScreen() {

    var selectedColor by remember {
        mutableStateOf(Color.Black)
    }

    var lastTouchX by remember {
        mutableStateOf(0f)
    }
    var lastTouchY by remember {
        mutableStateOf(0f)
    }
    val tempPath = remember {
        mutableStateOf<Line?>(Line())
    }
    var path = Line()
    val lineList = remember {
       mutableStateListOf<Line>()
    }
    Scaffold(topBar = {
        CanvasNavigationBar(selectedColor) {
            selectedColor = it
        }
    }) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .padding(it)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        path.path.moveTo(it.x, it.y)
                        path.color = selectedColor
                        tempPath.value?.path?.moveTo(it.x, it.y)
                        tempPath.value?.color = selectedColor
                        lastTouchX = it.x
                        lastTouchY = it.y
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val historySize = it.historySize
                        for (i in 0 until historySize) {
                            val historyX = it.getHistoricalX(i)
                            val historyY = it.getHistoricalY(i)
                            path.path.lineTo(historyX, historyY)
                            tempPath.value?.path?.lineTo(historyX, historyY)
                        }
                        path.path.lineTo(it.x, it.y)
                        tempPath.value?.path?.lineTo(it.x, it.y)
                        lastTouchX = it.x
                        lastTouchY = it.y
                    }
                    MotionEvent.ACTION_UP -> {
                        lineList.add(
                            Line(
                                path.path,
                                path.color
                            )
                        )
                        path = Line()
                    }
                }

                lastTouchX = it.x
                lastTouchY = it.y
                val savePath = tempPath.value
                tempPath.value = null
                tempPath.value = savePath
                true
            }) {
            tempPath.let {
                drawPath(
                    path = it.value?.path ?: Path(),
                    color = it.value?.color ?: Color.Black,
                    style = Stroke(
                        width = 4.dp.toPx()
                    )
                )
            }
            lineList.forEach {
                drawPath(
                    path = it.path,
                    color = it.color,
                    style = Stroke(
                        width = 4.dp.toPx()
                    )
                )
            }

        }
    }
}