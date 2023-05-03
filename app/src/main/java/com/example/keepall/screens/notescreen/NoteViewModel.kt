package com.example.keepall.screens.notescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepall.data.Note
import com.example.keepall.database.NoteDao
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteDao: NoteDao) : ViewModel() {


    private val _canvasFilePath = MutableStateFlow<String>("")
    val canvasFilePath = _canvasFilePath.asStateFlow()
    private val _pickedPhotos = MutableStateFlow<Array<String>>(emptyArray())
    val pickedPhotos = _pickedPhotos.asStateFlow()


    fun addPainting(path: String?) {
        viewModelScope.launch {
            flow {
                emit(path)
            }.collect {
                _canvasFilePath.value = it ?: ""
            }
        }
    }


    fun addNewPhoto(photoPath: String) {
        viewModelScope.launch {
            flow {
                emit(photoPath)
            }.collect {
                _pickedPhotos.value = pickedPhotos.value.plus(it)
            }
        }
    }

    fun addNewPhoto(photosPath: Array<String>) {
        viewModelScope.launch {
            flow {
                emit(photosPath)
            }.collect { paths ->
                    _pickedPhotos.value = paths
            }
        }
    }

    fun addNewNote(textContent: String) {
        val jsonParser = Moshi.Builder().build().adapter(Array<String>::class.java)
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insertNote(
                Note(
                    textContent = textContent,
                    canvas = _canvasFilePath.value,
                    photos = jsonParser.toJson(pickedPhotos.value)
                )
            )
        }
    }


}