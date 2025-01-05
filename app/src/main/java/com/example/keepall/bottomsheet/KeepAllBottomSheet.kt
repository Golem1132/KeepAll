package com.example.keepall.bottomsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.example.keepall.bottombar.BottomBarDialogType
import com.example.keepall.bottombar.BottomBarState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun KeepAllBottomSheet(
    state: BottomBarState,
    content: @Composable ColumnScope.() -> Unit,
    onDismiss: () -> Unit
) {
    val density = LocalDensity.current.density
    val maxDp = LocalConfiguration.current.screenHeightDp
    val max = maxDp * density
    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInteropFilter { motionEvent ->
            if (state.isDialogVisible) {
                val y = motionEvent.y
                val maxY = max / 2
                if (y < maxY)
                    onDismiss()
                true
            } else
                false
        }
        .then(
            if (state.isDialogVisible)
                Modifier.background(
                    brush = Brush.linearGradient(listOf(Color.Black, Color.Black)),
                    alpha = 0.2f
                )
            else Modifier
        ),
        contentAlignment = Alignment.BottomStart) {
        AnimatedVisibility(
            visible = state.isDialogVisible,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    content()
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewKeepAllBottomSheet() {
    Surface {
        KeepAllBottomSheet(
            state = BottomBarState.NoteMenu(),
            content = {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .background(Color.Red)
                )
            },
            onDismiss = {}
        )
    }
}