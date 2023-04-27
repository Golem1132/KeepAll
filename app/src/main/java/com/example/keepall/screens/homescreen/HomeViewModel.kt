package com.example.keepall.screens.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepall.data.Note
import com.example.keepall.database.NoteDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val noteDao: NoteDao): ViewModel() {

    private val _notesList = MutableStateFlow<List<Note>>(emptyList())
    val notesList = _notesList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.getAllNotesAsFlow().distinctUntilChanged()
                .collect {
                    _notesList.value = it
                }
        }
    }
}