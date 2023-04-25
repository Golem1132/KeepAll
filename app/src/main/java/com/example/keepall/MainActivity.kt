package com.example.keepall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.keepall.navigation.Navigation
import com.example.keepall.ui.theme.KeepAllTheme
import dagger.hilt.android.AndroidEntryPoint

private val permissions = arrayOf(
    android.Manifest.permission.CAMERA,
    android.Manifest.permission.READ_EXTERNAL_STORAGE,
    android.Manifest.permission.READ_MEDIA_IMAGES
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeepAllTheme {
                val permissionsHandler = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) {}
                LaunchedEffect(key1 = true) {
                    permissionsHandler.launch(permissions)
                }
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    Surface(modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background) {
        Navigation()
    }
}