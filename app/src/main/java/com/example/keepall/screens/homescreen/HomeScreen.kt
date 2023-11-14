package com.example.keepall.screens.homescreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keepall.components.AddNoteFAB
import com.example.keepall.components.NoteSnippet
import com.example.keepall.internal.HomeScreenEvent
import com.example.keepall.navigation.Screens
import com.example.keepall.topbar.KeepAllTopAppBar


enum class HomeScreenMode {
    PreviewMode,
    DeletionMode
}

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val homeScreenMode = viewModel.homeScreenMode.collectAsState()
    val notesList = viewModel.notesList.collectAsState()
    val checkedItems = viewModel.checkedItems.collectAsState()
    val isVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val searchText = rememberSaveable {
        mutableStateOf("")
    }
    Scaffold(topBar = {
        AnimatedContent(
            targetState = homeScreenMode.value,
            label = "TopAppBarStateAnimation",
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }
        ) { state ->
            when (state) {
                HomeScreenMode.PreviewMode ->
                    KeepAllTopAppBar(
                        mainIcon = {
                            Icon(
                                modifier = Modifier.clickable {
                                    isVisible.value = !isVisible.value
                                },
                                imageVector = Icons.Default.Search,
                                contentDescription = "Expand search field"
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
                        title = {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 10.dp),
                                value = searchText.value,
                                onValueChange = {
                                    searchText.value = it
                                    viewModel.search(searchText.value)
                                },
                                singleLine = true
                            )
                        },
                        isTitleVisible = isVisible.value
                    )

                else ->
                    KeepAllTopAppBar(
                        exitIcon = {
                            Icon(
                                modifier = Modifier.clickable {
                                    viewModel.performUiEvent(HomeScreenEvent.EXIT)
                                },
                                imageVector = Icons.Default.Close,
                                contentDescription = "Exit deletion mode"
                            )
                        },
                        deleteIcon = {
                            Icon(
                                modifier = Modifier.clickable {
                                    viewModel.performUiEvent(HomeScreenEvent.DELETE)
                                },
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Exit deletion mode"
                            )
                        },
                        count = checkedItems.value.size
                    )
            }
        }
    },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (homeScreenMode.value == HomeScreenMode.PreviewMode)
                AddNoteFAB(navController = navController)
        }) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(10.dp),
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = notesList.value) { noteListItem ->
                NoteSnippet(
                    item = noteListItem,
                    onLongClick = {
                        if (homeScreenMode.value == HomeScreenMode.PreviewMode) {
                            viewModel.addRemoveFromChecked(noteListItem.id)
                            viewModel.switchToMode(HomeScreenMode.DeletionMode)
                        }
                    },
                    onClick = {
                        if (homeScreenMode.value == HomeScreenMode.PreviewMode)
                            navController.navigate("${Screens.NoteScreen.route}?id=${noteListItem.id}")
                        else viewModel.addRemoveFromChecked(noteListItem.id)
                    },
                    isMarked = checkedItems.value.contains(noteListItem.id)
                )
            }

        }
    }
}