package com.example.keepall.screens.notescreen

import android.text.SpannableStringBuilder
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.keepall.R
import com.example.keepall.bottombar.BottomBarDialogType
import com.example.keepall.bottombar.BottomBarState
import com.example.keepall.bottombar.NoteModificationMenu
import com.example.keepall.bottombar.TextModificationMenu
import com.example.keepall.bottomsheet.KeepAllBottomSheet

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

    val noteTextFieldValue = viewModel.noteTextFieldValue.collectAsState()
    val noteTextFieldScroll = rememberScrollState()

    val contentScrollState = rememberScrollState()
    val currentStyle = viewModel.currentStyle.collectAsState()

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
                            state = currentStyle.value,
                            h1 = { viewModel.setFontSize(1.25f) },
                            h2 = { viewModel.setFontSize(1.15f) },
                            normalText = { viewModel.setFontSize(1f) },
                            bold = { viewModel.setBold(!currentStyle.value.bold) },
                            italic = { viewModel.setItalic(!currentStyle.value.italic) },
                            underline = { viewModel.setUnderline(!currentStyle.value.underline) },
                            clear = { viewModel.clearStyle() },
                            exit = {
                                viewModel.setMenuType(BottomBarState.NoteMenu())
                            }
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
                        .verticalScroll(contentScrollState)
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
                        value = noteTextFieldValue.value, onValueChange = { newText ->
                            viewModel.updateNoteTextField(newText)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(noteTextFieldScroll),
                        visualTransformation = {
                            TransformedText(
                            buildAnnotatedString {
                                viewModel.styledText.forEachIndexed() { index, style ->
                                    withStyle(SpanStyle(
                                        fontSize = (16 * style.fontSize).sp,
                                        fontWeight = if (style.bold) FontWeight.Bold else null,
                                        textDecoration = if (style.underline) TextDecoration.Underline else null,
                                        fontStyle = if (style.italic) FontStyle.Italic else null
                                        )) {
                                        append(viewModel.noteTextFieldValue.value.text[index])
                                    }
                                }
                            }, OffsetMapping.Identity)
                        },
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