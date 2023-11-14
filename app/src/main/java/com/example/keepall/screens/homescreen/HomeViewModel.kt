package com.example.keepall.screens.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepall.data.Note
import com.example.keepall.database.NoteDao
import com.example.keepall.internal.HomeScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val noteDao: NoteDao) : ViewModel() {

    private val _notesList = MutableStateFlow<List<Note>>(emptyList())
    val notesList = _notesList.asStateFlow()

    private val _checkedItems = MutableStateFlow<List<Int>>(emptyList())
    val checkedItems = _checkedItems.asStateFlow()

    private val _homeScreenMode = MutableStateFlow(HomeScreenMode.PreviewMode)
    val homeScreenMode = _homeScreenMode.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.getAllNotesAsFlow()
                .collect { collected ->
                    _notesList.value = collected.sortedByDescending {
                        it.dateAdded
                    }
                }
        }
    }

    fun search(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (query.isBlank())
                _notesList.value = noteDao.getAllNotes().apply {
                    sortedByDescending {
                        it.dateAdded
                    }
                }
            else
                _notesList.value = noteDao.searchFor(query).apply {
                    sortedByDescending {
                        it.dateAdded
                    }
                }
        }
    }

    fun addRemoveFromChecked(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_checkedItems.value.contains(id))
                _checkedItems.value = _checkedItems.value.minus(id)
            else
                _checkedItems.value = _checkedItems.value.plus(id)
        }
    }

    private fun uncheckEveryItem() {
        viewModelScope.launch(Dispatchers.IO) {
            _checkedItems.value = listOf()
        }
    }

    fun performUiEvent(uiEvent: HomeScreenEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                HomeScreenEvent.NO_ACTION -> {}
                HomeScreenEvent.DELETE -> {
                    deleteCheckedItems()
                    uncheckEveryItem()
                    _homeScreenMode.value = HomeScreenMode.PreviewMode
                }

                HomeScreenEvent.EXIT -> {
                    _homeScreenMode.value = HomeScreenMode.PreviewMode
                    uncheckEveryItem()
                }
            }
        }
    }

    fun switchToMode(mode: HomeScreenMode) {
        _homeScreenMode.value = mode
    }

    private fun deleteCheckedItems() {
        viewModelScope.launch(Dispatchers.IO) {
            for (id in _checkedItems.value) {
                _notesList.value.find {
                    it.id == id
                }.let {
                    noteDao.deleteNote(it ?: return@let)
                }
            }
        }
    }
}