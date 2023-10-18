package com.example.keepall.topbar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeepAllTopAppBar(
    mainIcon: @Composable () -> Unit,
    secondaryIcon: @Composable () -> Unit,
    actionIcon: @Composable () -> Unit = {},
    title: @Composable () -> Unit,
    isTitleVisible: Boolean
) {
    TopAppBar(
        modifier = Modifier,
        windowInsets = WindowInsets(left = 16.dp, right = 16.dp),
        title = {
            AnimatedVisibility(
                enter = expandHorizontally() + fadeIn(),
                exit = shrinkHorizontally() + fadeOut(),
                visible = isTitleVisible
            ) {
                title()
            }
        },
        navigationIcon = {
            AnimatedContent(targetState = isTitleVisible,
                label = "Icon",
                transitionSpec = {
                    scaleIn() togetherWith scaleOut()
                }
            ) { visibility ->
                if (visibility) {
                    Row {
                        secondaryIcon()
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                } else {
                    Row {
                        mainIcon()
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }

        },
        actions = {
            if (isTitleVisible) {
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    actionIcon()
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeepAllTopAppBar(
    exitIcon: @Composable () -> Unit,
    deleteIcon: @Composable () -> Unit,
    count: Int
) {
    TopAppBar(
        modifier = Modifier,
        title = {
            Text(text = count.toString())
        },
        navigationIcon = {
            exitIcon()
        },
        actions = {
            deleteIcon()
        }
    )
}


@Preview
@Composable
fun PreviewKeepAllTopAppBar() {
    val isVisible = rememberSaveable {
        mutableStateOf(true)
    }
    val searchText = rememberSaveable {
        mutableStateOf("")
    }
    KeepAllTopAppBar(
        mainIcon = {
            Icon(
                modifier = Modifier.clickable {
                    isVisible.value = !isVisible.value
                },
                imageVector = Icons.Default.Search,
                contentDescription = "Collapse search field"
            )
        },
        secondaryIcon = {
            Icon(
                modifier = Modifier.clickable {
                    isVisible.value = !isVisible.value
                },
                imageVector = Icons.Default.Close,
                contentDescription = "Collapse search field"
            )
        },
        actionIcon = {
            Icon(
                modifier = Modifier.clickable {
                    isVisible.value = !isVisible.value
                },
                imageVector = Icons.Default.Close,
                contentDescription = "Collapse search field"
            )
        },
        title = {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchText.value,
                onValueChange = {
                    searchText.value = it
                },
                singleLine = true
            )
        },
        isTitleVisible = isVisible.value
    )
}