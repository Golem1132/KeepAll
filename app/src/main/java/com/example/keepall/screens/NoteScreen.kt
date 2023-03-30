package com.example.keepall.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keepall.CanvasActivity

@Composable
fun NoteScreen() {
    val textState = remember {
        mutableStateOf("")
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(bottomBar = {
            NoteScreenBottomBar()
        }) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                TextField(
                    value = textState.value, onValueChange = {
                        textState.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.8f)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
    }
}

@Composable
fun NoteScreenBottomBar() {
    val xd = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()){result ->
        println(result.resultCode)
        println(result.data)
    }
    val localContext = LocalContext.current
    BottomAppBar(
        actions = {
            Icon(imageVector = Icons.Default.Build, contentDescription = "",
            modifier = Modifier.clickable {
                xd.launch(Intent(localContext, CanvasActivity::class.java))
            })
            Icon(imageVector = Icons.Default.Build, contentDescription = "")
            Icon(imageVector = Icons.Default.Build, contentDescription = "")
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
            }
        }
    )
}