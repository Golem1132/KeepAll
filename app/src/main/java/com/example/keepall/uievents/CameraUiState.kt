package com.example.keepall.uievents

import android.graphics.Bitmap

sealed class CameraUiState {
    data class Error(val msg: String): CameraUiState()

    data class Success(val bitmap: Bitmap, val path: String): CameraUiState()

    data class Idle(val bitmap: Bitmap? = null): CameraUiState()
}