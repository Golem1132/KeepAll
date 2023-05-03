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


    var canvasFilePath: String? = ""
    private val _pickedPhotos = MutableStateFlow<Array<String>>(emptyArray())
    val pickedPhotos = _pickedPhotos.asStateFlow()


    fun addNewPhoto(photoPath: String) {
        viewModelScope.launch {
            flow {
                emit(photoPath)
            }.collect {
                _pickedPhotos.value = pickedPhotos.value.plus(it)
            }
        }
    }

    fun addNewNote(textContent: String) {
        val jsonParser = Moshi.Builder().build().adapter(Array<String>::class.java)
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insertNote(
                Note(
                    textContent = textContent,
                    canvas = canvasFilePath,
                    photos = jsonParser.toJson(pickedPhotos.value)
                )
            )
        }
    }


}