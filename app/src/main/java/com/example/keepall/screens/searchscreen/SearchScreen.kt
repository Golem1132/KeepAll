package com.example.keepall.screens.searchscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.keepall.components.NoteSnippet
import com.example.keepall.navigation.Screens
import com.example.keepall.navigationbar.KeepAllNavigationBar
import com.example.keepall.screens.homescreen.navItemsList

@Composable
fun SearchScreen(navController: NavController) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val result = viewModel.result.collectAsState()
    val query = remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            KeepAllNavigationBar {
                NoteSearchBar(query = query.value, onQueryChange = {
                    query.value = it
                    viewModel.search(it)
                })
            }
        },
        bottomBar = {
            KeepAllNavigationBar {
                TabRow(selectedTabIndex = 1) {
                    navItemsList.forEach {
                        Tab(selected = it.route == navController.currentDestination?.route,
                            onClick = { navController.navigate(it.route) },
                            icon = {
                                Icon(imageVector = it.icon, contentDescription = "")
                            })
                    }
                }
            }
        }) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(10.dp),
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = result.value) { note ->
                NoteSnippet(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5))
                        .height(200.dp)
                        .clickable {
                            navController.navigate("${Screens.NoteScreen.route}?id=${note.id}")
                        },
                    item = note
                )
            }

        }
    }
}


@Composable
fun NoteSearchBar(query: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier.padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
        OutlinedTextField(value = query, onValueChange = {
            onQueryChange(it)
        }, modifier = Modifier.fillMaxWidth())
    }
}