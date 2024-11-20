package com.example.keepall.topbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.keepall.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeepAllTopBarEdit(
    count: Int,
    onPinClick: () -> Unit,
    onPaletteClick: () -> Unit,
    onAlertClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onExit: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable {
                        onExit()
                    },
                imageVector = Icons.Default.Clear, contentDescription = "Exit"
            )
        },
        title = {
            Text(text = "$count")
        },
        actions = {
            Icon(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        onPinClick()
                    },
                painter = painterResource(id = R.drawable.baseline_push_pin_24),
                contentDescription = "Pin checked notes to the top"
            )
            Icon(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        onPaletteClick()
                    },
                painter = painterResource(id = R.drawable.palette_24px),
                contentDescription = "Change background color to picked notes"
            )
            Icon(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        onAlertClick()
                    },
                painter = painterResource(id = R.drawable.baseline_add_alert_24),
                contentDescription = "Add alert to checked notes"
            )
            Icon(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        onDeleteClick()
                    },
                painter = painterResource(id = R.drawable.baseline_delete_24),
                contentDescription = "Delete checked notes"
            )
        })
}

@Preview
@Composable
fun PreviewKeepAllTopBarEdit() {
    KeepAllTopBarEdit(
        1,
        {},
        {},
        {},
        {},
        {}
    )
}