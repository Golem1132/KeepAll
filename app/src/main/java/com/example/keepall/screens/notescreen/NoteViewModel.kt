package com.example.keepall.screens.notescreen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepall.data.Note
import com.example.keepall.database.NoteDao
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteDao: NoteDao, savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val jsonParser = Moshi.Builder().build().adapter(Array<String>::class.java)
    var id: Int?
    private val _titleState = MutableStateFlow("")
    val titleState = _titleState.asStateFlow()
    private val _colorState = MutableStateFlow(Color.White)
    val colorState = _colorState.asStateFlow()
    private val _textState = MutableStateFlow("")
    val textState = _textState.asStateFlow()
    private val _canvasFilePath = MutableStateFlow<String>("")
    val canvasFilePath = _canvasFilePath.asStateFlow()
    private val _pickedPhotos = MutableStateFlow<Array<String>>(emptyArray())
    val pickedPhotos = _pickedPhotos.asStateFlow()
    private val _attachmentsList = MutableStateFlow<Array<String>>(emptyArray())
    val attachmentsList = _attachmentsList.asStateFlow()


    init {
        id = savedStateHandle.get<Int>("id")
        viewModelScope.launch(Dispatchers.IO) {
            if (id != null && id!! > 0) {
                noteDao.getNote(id!!).let {
                    _textState.value = it.textContent
                    _canvasFilePath.value = it.canvas ?: ""
                    _pickedPhotos.value = jsonParser.fromJson(it.photos ?: "") ?: arrayOf()
                }
            }
        }
    }


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
        viewModelScope.launch(Dispatchers.IO) {
            if (id != null && id!! > 0)
                noteDao.updateNote(
                    Note(
                        id = id!!,
                        textContent = textContent,
                        canvas = _canvasFilePath.value,
                        photos = jsonParser.toJson(pickedPhotos.value),
                        dateAdded = Date()
                    )
                )
            else
                noteDao.insertNote(
                    Note(
                        textContent = textContent,
                        canvas = _canvasFilePath.value,
                        photos = jsonParser.toJson(pickedPhotos.value),
                        dateAdded = Date()
                    )
                )
        }
    }

    fun updateTextState(newValue: String) {
        _textState.value = newValue
    }

    fun updateTitleState(newValue: String) {
        _titleState.value = newValue
    }

    fun setColor(pickedColor: Int) {
        viewModelScope.launch {
            _colorState.emit(Color(pickedColor))
        }
    }

}