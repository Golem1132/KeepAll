package com.example.keepall.screens.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.keepall.components.NoteSnippet
import com.example.keepall.navigation.Screens
import com.example.keepall.topbar.KeepAllTTopBar
import com.example.keepall.topbar.KeepAllTopBarEdit
import com.example.keepall.topbar.rememberKeepAllTopBarState
import org.koin.androidx.compose.koinViewModel


enum class HomeScreenMode {
    PreviewMode,
    DeletionMode
}

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = koinViewModel()
    val homeScreenMode = viewModel.homeScreenMode.collectAsState()
    val notesList = viewModel.notesList.collectAsState()
    val checkedItems = viewModel.checkedItems.collectAsState()
    val keepAllTopBarState = rememberKeepAllTopBarState()
    Scaffold(topBar = {
        if (homeScreenMode.value == HomeScreenMode.PreviewMode)
            KeepAllTTopBar(state = keepAllTopBarState,
                onKeyboardAction = { query ->
                    TODO("Search from viewmodel")
                },
                onActionClick = {
                    TODO("Go to profile/config")
                })
        else KeepAllTopBarEdit(
            count = checkedItems.value.size,
            onPinClick = { /*TODO*/ },
            onPaletteClick = { /*TODO*/ },
            onAlertClick = { /*TODO*/ },
            onDeleteClick = { /*TODO*/ }) {

        }
    },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (homeScreenMode.value == HomeScreenMode.PreviewMode)
                FloatingActionButton(onClick = {
                    navController.navigate(route = Screens.NoteScreen.route)
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
                }
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