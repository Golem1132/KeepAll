package com.example.keepall.screens.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class CameraViewModel: ViewModel() {

    private val _cameraUiState: MutableStateFlow<CameraUiState> = MutableStateFlow(CameraUiState.Idle())

    val cameraUiState: StateFlow<CameraUiState> = _cameraUiState

    fun sendError(msg: String) {
        viewModelScope.launch {
            _cameraUiState.emit(CameraUiState.Error(msg))
        }
    }

    fun sendPhoto(file: File) {
        viewModelScope.launch {
            _cameraUiState.emit(CameraUiState.Success(BitmapFactory.decodeFile(file.canonicalPath)))
        }
    }

    fun goBackToPreview() {
        viewModelScope.launch {
            _cameraUiState.emit(CameraUiState.Idle())
        }
    }

}

sealed class CameraUiState {
    data class Error(val msg: String): CameraUiState()

    data class Success(val bitmap: Bitmap): CameraUiState()

    data class Idle(val bitmap: Bitmap? = null): CameraUiState()
}