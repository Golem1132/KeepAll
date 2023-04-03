package com.example.keepall.screens.notescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File


class NoteViewModel : ViewModel() {
    private val _filesList = MutableStateFlow<List<File>>(emptyList())
    val filesList = _filesList



    fun add(file: File) {
        viewModelScope.launch {
            flow<File> {
                emit(file)
            }.collect() {
                val tempList = _filesList.value.toMutableList()
                tempList.add(file)
                _filesList.value = tempList
            }
        }

    }

}