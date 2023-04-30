package com.example.keepall.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CanvasBottomBar(
    onSave: () -> Unit = {},
    onExit: () -> Unit = {},
    onThicknessClicked: () -> Unit,
    onColorsClicked: () -> Unit
) {
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(painter = painterResource(id = com.example.keepall.R.drawable.line_weight_24px), contentDescription = "Thickness",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onThicknessClicked()
                    })
            Icon(painter = painterResource(id = com.example.keepall.R.drawable.palette_24px), contentDescription = "Colors",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onColorsClicked()
                    })
            Icon(painter = painterResource(id = com.example.keepall.R.drawable.delete_24px), contentDescription = "Discard",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onExit()
                    })
            Icon(painter = painterResource(id = com.example.keepall.R.drawable.save_24px), contentDescription = "Save",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onSave()
                    })
        }
    }
}