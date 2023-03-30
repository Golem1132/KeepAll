package com.example.keepall.screens.canvas

import android.graphics.Bitmap
import android.graphics.Paint
import androidx.compose.ui.graphics.asAndroidPath
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import com.example.keepall.model.Line
import kotlinx.coroutines.flow.MutableStateFlow

class CanvasViewModel: ViewModel() {
    private val _canvasStateFlow = MutableStateFlow<List<Line>>(listOf())
    val canvasStateFlow = _canvasStateFlow



    fun addNewLine(line: Line) {
        val temp = _canvasStateFlow.value.toMutableList()
        temp.add(line)
        _canvasStateFlow.value = temp.toList()
    }

    fun saveCanvasToJpg(width: Int, height: Int): Bitmap {
        val bitmap = createBitmap(width, height)
        return bitmap.applyCanvas {
            this.drawARGB(255,255,255,255)
            canvasStateFlow.value.forEach {
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                paint.style = Paint.Style.STROKE
                paint.color = it.color.hashCode()
                paint.strokeWidth = 4f
                this.drawPath(
                    it.path.asAndroidPath(),
                    paint
                )
            }
        }
    }
}