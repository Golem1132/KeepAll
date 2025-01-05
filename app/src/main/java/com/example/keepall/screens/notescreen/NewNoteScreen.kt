package com.example.keepall.screens.notescreen

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerDefaults.scrimColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue.Hidden
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.getSpans
import androidx.core.text.toSpannable
import androidx.core.text.toSpanned
import androidx.core.text.underline
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.keepall.R
import com.example.keepall.bottombar.BottomBarDialogType
import com.example.keepall.bottombar.BottomBarState
import com.example.keepall.bottombar.NoteModificationMenu
import com.example.keepall.bottombar.TextModificationMenu
import com.example.keepall.bottomsheet.KeepAllBottomSheet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun NewNoteScreen(navController: NavController) {
    val viewModel: NewNoteScreenViewModel = viewModel()
    val bottomBarState = viewModel.bottomBarState.collectAsState()
    var titleTextFieldValue by remember {
        mutableStateOf(TextFieldValue())
    }
    val titleTextFieldScroll = rememberScrollState()
    var noteTextFieldValue by remember {
        mutableStateOf(TextFieldValue())
    }
    val noteTextFieldScroll = rememberScrollState()
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            TODO("SAVE AND EXIT")
                        },
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = ""
                    )
                },
                actions = {
                    Icon(
                        modifier = Modifier.clickable {
                            TODO("ADD TO CALENDAR ")
                        },
                        painter = painterResource(id = R.drawable.baseline_add_alert_24),
                        contentDescription = ""
                    )
                }
            )
        }
    ) { paddingValues ->
        SubcomposeLayout(
            modifier = Modifier.padding(paddingValues)
        ) { constraints ->
            val bottomBar = subcompose(1) {
                BottomAppBar(actions = {
                    if (bottomBarState.value is BottomBarState.TextMenu)
                        TextModificationMenu(
                            h1 = { /*TODO*/ },
                            h2 = { /*TODO*/ },
                            normalText = { /*TODO*/ },
                            bold = { /*TODO*/ },
                            italic = { /*TODO*/ },
                            underline = { /*TODO*/ },
                            clear = { /*TODO*/ },
                            exit = {
                                viewModel.setMenuType(BottomBarState.NoteMenu())
                            },
                            currentStyle = SpanStyle()
                        )
                    else
                        NoteModificationMenu(
                            addIconAction = {
                                viewModel.toggleDialogVisibility(
                                    BottomBarDialogType.ADD_ATTACHMENT
                                )
                            },
                            paletteIconAction = {
                                viewModel.toggleDialogVisibility(
                                    BottomBarDialogType.BACKGROUND_COLOR
                                )
                            },
                            textIconAction = {
                                viewModel.setMenuType(BottomBarState.TextMenu())
                            })

                }, floatingActionButton = {
                    if (bottomBarState.value !is BottomBarState.TextMenu)
                        FloatingActionButton(onClick = { }) {
                            Text(text = "Save")
                        }
                })
            }.map {
                it.measure(constraints)
            }
            val bottomBarHeight = bottomBar.fold(0) { acc, placeable ->
                acc + placeable.height

            }
            val mainConstraints = constraints.copy(
                maxHeight = constraints.maxHeight - bottomBarHeight
            )
            val mainContent = subcompose(0) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    FlowColumn(modifier = Modifier.fillMaxWidth()) {

                    }
                    TextField(
                        value = titleTextFieldValue,
                        onValueChange = { newText ->
                            titleTextFieldValue = newText
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(titleTextFieldScroll),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = {
                            Text(text = "Title")
                        }
                    )

                    TextField(
                        value = noteTextFieldValue, onValueChange = { newText ->
                            noteTextFieldValue = newText
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(noteTextFieldScroll),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = {
                            Text(text = "Note")
                        }
                    )

                }
            }
                .map {
                    it.measure(mainConstraints)
                }

            layout(constraints.maxWidth, constraints.maxHeight) {
                mainContent.map {
                    it.place(0, 0)
                }
                bottomBar.map {
                    it.place(0, constraints.maxHeight - bottomBarHeight)
                }
            }
        }
    }
    KeepAllBottomSheet(state = bottomBarState.value,
        content = {

        },
        onDismiss = {
            viewModel.toggleDialogVisibility()
        })
}

@Preview
@Composable
fun PreviewNoteScreen() {
    NewNoteScreen(navController = rememberNavController())
}