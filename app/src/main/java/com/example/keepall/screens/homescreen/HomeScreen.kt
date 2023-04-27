package com.example.keepall.screens.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.keepall.components.AddNoteFAB
import com.example.keepall.components.NoteSnippet
import com.example.keepall.navigationbar.KeepAllNavigationBar
import com.example.keepall.screens.homescreen.HomeViewModel
import com.example.keepall.ui.theme.Yellow20

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

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(10.dp),
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(notesList.value) {note ->
                NoteSnippet(modifier = Modifier
                    .clip(RoundedCornerShape(5))
                    .height(200.dp)
                    .clickable {  },
                    note = note)
            }

        }

    }
}