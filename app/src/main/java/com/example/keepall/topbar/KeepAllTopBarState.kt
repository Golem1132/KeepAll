package com.example.keepall.topbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable

class KeepAllTopBarState(
    initSearch: String = "",
    initSearchFocused: Boolean = false
) {
    companion object {
        val Saver: Saver<KeepAllTopBarState, Any> = mapSaver(
            save = { keepAllTopBarState ->
                mapOf(
                    "search" to keepAllTopBarState.search.value,
                    "isSearchFocused" to keepAllTopBarState.isSearchFocused.value
                )
            },
            restore = { map ->
                KeepAllTopBarState(
                    map["search"] as String,
                    map["isSearchFocused"] as Boolean
                )
            }
        )
    }

    var search = mutableStateOf(initSearch)
    var isSearchFocused = mutableStateOf(initSearchFocused)

}

@Composable
fun rememberKeepAllTopBarState(): KeepAllTopBarState {
    return rememberSaveable(saver = KeepAllTopBarState.Saver) {
        KeepAllTopBarState()
    }
}