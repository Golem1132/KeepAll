package com.example.keepall.screens.notescreen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val _attachmentsList = MutableStateFlow<Array<String>>(emptyArray())
    val attachmentsList = _attachmentsList.asStateFlow()


    init {
        id = savedStateHandle.get<Int>("id")
        viewModelScope.launch(Dispatchers.IO) {
            if (id != null && id!! > 0) {
                noteDao.getNote(id!!).let {
                    _textState.value = it.textContent

                }
            }
        }
    }

    fun updateTextState(newValue: String) {
        _textState.value = newValue
    }

    fun updateTitleState(newValue: String) {
        _titleState.value = newValue
    }

    fun setColor(pickedColor: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _colorState.emit(Color(pickedColor))
        }
    }

    fun updateAttachmentsList(path: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!path.isNullOrEmpty()) {
                val newList = _attachmentsList.value.plus(path)
                _attachmentsList.value = newList
            }
        }
    }

    fun updateAttachmentsList(paths: Array<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            var newList = _attachmentsList.value
            paths.forEach {
                newList = newList.plus(it)
            }
            _attachmentsList.value = newList
        }
    }

    fun saveNote() {

    }

}