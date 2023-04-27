package com.example.keepall.screens.homescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.keepall.components.AddNoteFAB
import com.example.keepall.components.NoteSnippet
import com.example.keepall.navigationbar.KeepAllNavigationBar
import com.example.keepall.screens.homescreen.HomeViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {
    val notesList = viewModel.notesList.collectAsState()
    Scaffold(bottomBar = {
        KeepAllNavigationBar(navController = navController)
    },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddNoteFAB(navController = navController)
        }) {
        LazyColumn(modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(10.dp)
        ) {
            items(notesList.value) {note ->
                NoteSnippet(note) {

                }
            }

        }

    }
}