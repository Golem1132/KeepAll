package com.example.keepall.screens.camera

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepall.uievents.CameraUiState
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

    fun sendPhoto(path: String) {
        viewModelScope.launch {
            _cameraUiState.emit(CameraUiState.Success(BitmapFactory.decodeFile(path), path))
        }
    }

    fun goBackToPreview() {
        viewModelScope.launch {
            _cameraUiState.emit(CameraUiState.Idle())
        }
    }

}