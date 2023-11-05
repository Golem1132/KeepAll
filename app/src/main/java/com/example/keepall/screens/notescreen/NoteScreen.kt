package com.example.keepall.screens.notescreen

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.keepall.R
import com.example.keepall.bottombar.NoteModificationMenu
import com.example.keepall.bottombar.TextModificationMenu
import com.example.keepall.components.MenuItem
import com.example.keepall.constants.CANVAS_PATH
import com.example.keepall.constants.PHOTO_PATH
import com.example.keepall.constants.PICKED_PHOTOS
import com.example.keepall.internal.BottomBarMode
import com.example.keepall.internal.BottomSheetContent
import com.example.keepall.screens.camera.CameraActivity
import com.example.keepall.screens.canvas.CanvasActivity
import com.example.keepall.screens.gallery.GalleryActivity
import com.example.keepall.ui.theme.Typography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val localContext = LocalContext.current
    val viewModel = hiltViewModel<NoteViewModel>()
    val bottomBarState = remember {
        mutableStateOf(BottomBarMode.NOTE_OPERATIONS)
    }
    val attachmentsList = viewModel.attachmentsList.collectAsState()
    val sheetVisibility = remember {
        mutableStateOf(false)
    }
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data?.getStringExtra(PHOTO_PATH) != null)
                    viewModel.updateAttachmentsList(result.data!!.getStringExtra(PHOTO_PATH)!!)
            }
        }
    val canvasLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.updateAttachmentsList(result.data?.getStringExtra(CANVAS_PATH))
            }
        }

    val filePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data?.getStringArrayExtra(PICKED_PHOTOS) != null) {
                    viewModel.updateAttachmentsList(result.data!!.getStringArrayExtra(PICKED_PHOTOS)!!)
                }
            }
        }

    val filePermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val intent = Intent(
                    localContext,
                    GalleryActivity::class.java
                )
                filePickerLauncher.launch(intent)
            }
        }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                cameraLauncher.launch(
                    Intent(
                        localContext,
                        CameraActivity::class.java
                    )
                )
            }
        }
    val titleState = viewModel.titleState.collectAsState()
    val textState = viewModel.textState.collectAsState()
    val currentColor = viewModel.colorState.collectAsState()
    val currentSheetContent = remember {
        mutableStateOf(BottomSheetContent.NoteMenu)
    }

    val currentStyle = viewModel.currentStyle.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomAppBar(
                    actions = {
                        AnimatedContent(
                            targetState = bottomBarState.value,
                            label = "BottomBarAnimatedContent",
                            transitionSpec = {
                                slideInVertically {
                                    it
                                } togetherWith fadeOut()
                            }
                        ) { currentState ->
                            if (currentState == BottomBarMode.NOTE_OPERATIONS)
                                NoteModificationMenu(
                                    addIconAction = {
                                        scope.launch(Dispatchers.IO) {
                                            currentSheetContent.value = BottomSheetContent.NoteMenu
                                            sheetVisibility.value = true
                                            bottomSheetState.expand()
                                        }
                                    },
                                    paletteIconAction = {
                                        scope.launch(Dispatchers.IO) {
                                            currentSheetContent.value = BottomSheetContent.ColorMenu
                                            sheetVisibility.value = true
                                            bottomSheetState.expand()
                                        }
                                    },
                                    textIconAction = {
                                        bottomBarState.value = BottomBarMode.TEXT_OPERATIONS
                                    })
                            else
                                TextModificationMenu(
                                    h1 = {
                                        viewModel.styleList.add(currentStyle.value)
                                        viewModel.updateCurrentStyle(
                                            currentStyle.value.copy(
                                                currentStyle.value.item.copy(fontSize = Typography.headlineLarge.fontSize),
                                                textState.value.text.length,
                                                textState.value.text.length
                                            )
                                        )
                                    },
                                    h2 = {
                                        viewModel.styleList.add(currentStyle.value)
                                        viewModel.updateCurrentStyle(
                                            currentStyle.value.copy(
                                                currentStyle.value.item.copy(fontSize = Typography.headlineMedium.fontSize),
                                                textState.value.text.length,
                                                textState.value.text.length
                                            )
                                        )
                                    },
                                    normalText = {
                                        viewModel.styleList.add(currentStyle.value)
                                        viewModel.updateCurrentStyle(
                                            currentStyle.value.copy(
                                                currentStyle.value.item.copy(fontSize = TextUnit.Unspecified),
                                                textState.value.text.length,
                                                textState.value.text.length
                                            )
                                        )
                                    },
                                    bold = {
                                        viewModel.styleList.add(currentStyle.value)
                                        viewModel.updateCurrentStyle(
                                            currentStyle.value.copy(
                                                currentStyle.value.item.copy(fontWeight = FontWeight.Bold),
                                                textState.value.text.length,
                                                textState.value.text.length
                                            )
                                        )
                                    },
                                    italic = {
                                        viewModel.styleList.add(currentStyle.value)
                                        viewModel.updateCurrentStyle(
                                            currentStyle.value.copy(
                                                currentStyle.value.item.copy(fontStyle = FontStyle.Italic),
                                                textState.value.text.length,
                                                textState.value.text.length
                                            )
                                        )
                                    },
                                    underline = {
                                        viewModel.styleList.add(currentStyle.value)
                                        viewModel.updateCurrentStyle(
                                            currentStyle.value.copy(
                                                currentStyle.value.item.copy(textDecoration = TextDecoration.Underline),
                                                textState.value.text.length,
                                                textState.value.text.length
                                            )
                                        )
                                    },
                                    clear = {
                                        viewModel.styleList.add(currentStyle.value)
                                        viewModel.updateCurrentStyle(
                                            AnnotatedString.Range(
                                                SpanStyle(),
                                                textState.value.text.length,
                                                textState.value.text.length
                                            )
                                        )
                                    },
                                    exit = {
                                        bottomBarState.value = BottomBarMode.NOTE_OPERATIONS
                                    })
                        }
                    },
                    floatingActionButton = {

                        AnimatedContent(targetState = bottomBarState.value,
                            label = "BottomBarAnimatedContent",
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            }) { currentState ->
                            if (currentState == BottomBarMode.NOTE_OPERATIONS)
                                FloatingActionButton(onClick = {
                                    viewModel.styleList.add(currentStyle.value)
                                    viewModel.saveNote {
                                        navController.popBackStack()
                                    }
                                }) {
                                    Text(text = "Save")
                                }
                        }
                    }

                )
            },
            topBar = {
                Icon(imageVector = Icons.Default.Close,
                    contentDescription = "Discard note",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            navController.popBackStack()
                        })
            }) {
            if (sheetVisibility.value)
                ModalBottomSheet(
                    sheetState = bottomSheetState,
                    shape = RectangleShape,
                    dragHandle = {},
                    onDismissRequest = {
                        scope.launch(Dispatchers.IO) {
                            bottomSheetState.hide()
                        }.invokeOnCompletion {
                            sheetVisibility.value = false
                        }
                    }) {
                    val scrollState = rememberScrollState(
                        initial = 0
                    )
                    Column(modifier = Modifier.fillMaxWidth()) {
                        if (currentSheetContent.value == BottomSheetContent.ColorMenu) {
                            Row(
                                modifier = Modifier
                                    .horizontalScroll(
                                        state = scrollState
                                    ),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Spacer(modifier = Modifier.width(10.dp))
                                stringArrayResource(id = R.array.availableBackgrounds).forEach { color ->
                                    val parsedColor = android.graphics.Color.parseColor(color)
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(Color(parsedColor))
                                            .clickable {
                                                viewModel.setColor(parsedColor)
                                            }
                                            .then(
                                                if (parsedColor == currentColor.value.toArgb())
                                                    Modifier.border(
                                                        2.dp,
                                                        Color.Black,
                                                        CircleShape
                                                    )
                                                else
                                                    Modifier.border(2.dp, Color.Transparent)
                                            )
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                            }
                        } else {
                            MenuItem("Take photo") {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                            MenuItem("Add photo") {
                                if (Build.VERSION.SDK_INT > 32)
                                    filePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                                else
                                    filePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                            MenuItem("Add painting") {
                                canvasLauncher.launch(
                                    Intent(
                                        localContext,
                                        CanvasActivity::class.java
                                    )
                                )
                            }
                        }
                    }
                }
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                val maxHeight = this.maxHeight
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .heightIn(min = maxHeight),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LazyRow(modifier = Modifier.heightIn(min = 0.dp, max = maxHeight / 2)) {
                        items(attachmentsList.value) { path ->
                            AsyncImage(
                                contentScale = ContentScale.Fit,
                                model = File(path),
                                contentDescription = ""
                            )
                        }
                    }

                    TextField(
                        value = titleState.value, onValueChange = { newText ->
                            viewModel.updateTitleState(newText)
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
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
                    HorizontalDivider()
                    TextField(
                        value = textState.value, onValueChange = { newText ->
                            viewModel.updateTextState(newText)
                            val start = currentStyle.value.start
                            val end = newText.text.length
                            viewModel.updateCurrentStyle(
                                currentStyle.value.copy(
                                    start = if (start >= end) newText.text.length else start,
                                    end = end
                                )
                            )
                        },
                        visualTransformation = { _ ->
                            TransformedText(buildAnnotatedString {
                                for (index in 0 until viewModel.styleList.size) {
                                    if (viewModel.styleList[index].end > textState.value.text.length) {
                                        if (viewModel.styleList[index].start > textState.value.text.length) {
                                            viewModel.styleList.removeAt(index)
                                            continue
                                        } else {
                                            viewModel.styleList[index] =
                                                viewModel.styleList[index].copy(
                                                    end = viewModel.getText().length
                                                )
                                            addStyle(
                                                viewModel.styleList[index].item,
                                                viewModel.styleList[index].start,
                                                viewModel.styleList[index].end
                                            )
                                        }
                                    } else {
                                        addStyle(
                                            viewModel.styleList[index].item,
                                            viewModel.styleList[index].start,
                                            viewModel.styleList[index].end
                                        )
                                    }
                                }
                                addStyle(
                                    currentStyle.value.item,
                                    currentStyle.value.start,
                                    currentStyle.value.end
                                )
                                append(textState.value.text)
                            }, OffsetMapping.Identity)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(maxHeight),
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
        }
    }
}