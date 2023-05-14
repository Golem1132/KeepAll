package com.example.keepall.screens.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepall.data.Note
import com.example.keepall.database.NoteDao
import com.example.keepall.model.NavItemEvent
import com.example.keepall.model.NoteListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val noteDao: NoteDao) : ViewModel() {

    private val _notesList = MutableStateFlow<List<NoteListItem>>(emptyList())
    val notesList = _notesList.asStateFlow()

    private val _homeScreenMode = MutableStateFlow(HomeScreenMode.PreviewMode)
    val homeScreenMode = _homeScreenMode.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.getAllNotesAsFlow()
                .collect { collected ->
                    collected.map { note ->
                        NoteListItem(
                            note = note,
                            checked = false
                        )
                    }.let { mapped ->
                        _notesList.value = mapped.sortedByDescending {
                            it.note.id
                        }
                    }
                }
        }
    }

    fun addRemoveFromChecked(id: Int) {
        viewModelScope.launch {
            flow {
                emit(id)
            }.collect { emitedId ->
                val tempList = _notesList.value.toMutableList()
                _notesList.value.find { item ->
                    item.note.id == emitedId
                }?.let {
                    tempList.remove(it)
                    tempList.add(
                        it.copy(
                            note = it.note,
                            checked = !it.checked
                        )
                    )
                }
                _notesList.value = tempList.sortedByDescending {
                    it.note.id
                }
            }
        }
    }

    private fun uncheckEveryItem() {
        val tempList = _notesList.value.toMutableList()
        viewModelScope.launch(Dispatchers.IO) {
            for(item in tempList) {
                if (item.checked) {
                    tempList.remove(item)
                    tempList.add(item.copy(
                        note = item.note,
                        checked = false
                    ))
                } else continue

            }
            _notesList.value = tempList.sortedByDescending {
                it.note.id
            }
        }
    }

    fun performUiEvent(uiEvent: NavItemEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                NavItemEvent.NO_ACTION -> {}
                NavItemEvent.DELETE -> {
                    deleteCheckedItems()
                    _homeScreenMode.value = HomeScreenMode.PreviewMode
                }
                NavItemEvent.EXIT -> {
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
            _notesList.value.forEach {
                if (it.checked)
                    noteDao.deleteNote(it.note)
            }
        }
    }
}