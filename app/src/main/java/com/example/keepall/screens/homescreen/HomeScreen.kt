package com.example.keepall.screens.homescreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.keepall.components.AddNoteFAB
import com.example.keepall.components.NoteSnippet
import com.example.keepall.model.NavItem
import com.example.keepall.model.NavItemEvent
import com.example.keepall.navigation.Screens
import com.example.keepall.navigationbar.KeepAllNavigationBar

private val navItemsList = listOf(
    NavItem(
        Screens.HomeScreen.route,
        Icons.Default.List,
        NavItemEvent.NO_ACTION
    ),
    NavItem(
        Screens.SearchScreen.route,
        Icons.Default.Search,
        NavItemEvent.NO_ACTION
    ),
)

private val deletionModeItemsList = listOf(
    NavItem(
        "",
        Icons.Default.Close,
        NavItemEvent.EXIT
    ),
    NavItem(
        "",
        Icons.Default.Delete,
        NavItemEvent.DELETE
    )
)

enum class HomeScreenMode {
    PreviewMode,
    DeletionMode
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val homeScreenMode = viewModel.homeScreenMode.collectAsState()
    val notesList = viewModel.notesList.collectAsState()
    Scaffold(bottomBar = {
        KeepAllNavigationBar {
            if (homeScreenMode.value == HomeScreenMode.PreviewMode)
                TabRow(selectedTabIndex = 0) {
                    navItemsList.forEach {
                        Tab(selected = it.route == navController.currentDestination?.route,
                            onClick = { navController.navigate(it.route) },
                            icon = {
                                Icon(imageVector = it.icon, contentDescription = "")
                            })
                    }
                }
            else
                NavigationBar {
                    deletionModeItemsList.forEach {
                        NavigationBarItem(selected = it.route == navController.currentDestination?.route,
                            onClick = {
                            viewModel.performUiEvent(it.action)
                            },
                            icon = {
                                Icon(imageVector = it.icon, contentDescription = "")
                            })
                    }
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
                    modifier = Modifier
                        .clip(RoundedCornerShape(5))
                        .height(200.dp)
                        .combinedClickable(
                            onClick = {
                                if (homeScreenMode.value == HomeScreenMode.PreviewMode)
                                    navController.navigate("${Screens.NoteScreen.route}?id=${noteListItem.note.id}")
                                else viewModel.addRemoveFromChecked(noteListItem.note.id)
                            },
                            onLongClick = {
                                if (homeScreenMode.value == HomeScreenMode.PreviewMode) {
                                    viewModel.addRemoveFromChecked(noteListItem.note.id)
                                    viewModel.switchToMode(HomeScreenMode.DeletionMode)
                                }
                            }),
                    item = noteListItem
                )
            }

        }
    }
}