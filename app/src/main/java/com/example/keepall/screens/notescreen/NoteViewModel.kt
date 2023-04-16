package com.example.keepall.screens.notescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File


class NoteViewModel : ViewModel() {
    private val _filesList = MutableStateFlow<List<File>>(emptyList())
    val filesList = _filesList

    var canvasFilePath: String? = ""
    var photoFilePath: String? = ""

    val checkedPhotos = ArrayList<String>()



    fun fetchFile(file: File) {
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

    fun addFile(path: String) {
        when(checkedPhotos.contains(path)) {
            true -> {
                checkedPhotos.remove(path)
            }
            false -> {
                checkedPhotos.add(path)
            }
        }
        println(checkedPhotos)
    }

}