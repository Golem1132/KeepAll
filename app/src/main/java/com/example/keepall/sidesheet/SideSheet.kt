package com.example.keepall.sidesheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.keepall.ui.theme.Shadow

@Composable
fun SideSheet(
    state: SideSheetState,
    content: @Composable () -> Unit
) {
    if (state.currentValue.value == SideSheetValue.OPENED) {
        Row(modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Start) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .fillMaxHeight()
                    .background(color = Shadow)
                    .clickable(interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            state.close()
                        })
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = Color.White)
                    .clickable(interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {})
            ) {
                SideSheetTopBar() {
                    state.close()
                }
                content()
            }
        }
    }
}

@Composable
private fun SideSheetTopBar(onIconClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End) {
        Icon(
            imageVector = Icons.Default.Close, contentDescription = "Close side sheet",
            modifier = Modifier
                .size(48.dp)
                .clickable {
                    onIconClick()
                }
        )
    }
}