package com.example.keepall.screens.notescreen

import android.text.Html
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepall.data.Note
import com.example.keepall.database.NoteDao
import com.example.keepall.spannedconverter.extractStyle
import com.example.keepall.spannedconverter.toAnnotatedString
import com.example.keepall.spannedconverter.toHtml
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
    private val _textState = MutableStateFlow(TextFieldValue())
    val textState = _textState.asStateFlow()
    private val _attachmentsList = MutableStateFlow<Array<String>>(emptyArray())
    val attachmentsList = _attachmentsList.asStateFlow()

    private val _currentStyle =
        MutableStateFlow(AnnotatedString.Range<SpanStyle>(SpanStyle(), 0, 0))
    val currentStyle = _currentStyle.asStateFlow()
    val styleList = mutableListOf<AnnotatedString.Range<SpanStyle>>()


    init {
        id = savedStateHandle.get<Int>("id")
        viewModelScope.launch(Dispatchers.IO) {
            if (id != null && id!! > 0) {
                noteDao.getNote(id!!).let {
                    val extractedText = Html.fromHtml(it.textContent, Html.FROM_HTML_MODE_COMPACT)
                    _titleState.value = it.title
                    _textState.value = TextFieldValue(
                        extractedText.toAnnotatedString().text
                    )
                    _attachmentsList.value = jsonParser.fromJson(it.attachments) ?: emptyArray()
                    styleList.addAll(extractedText.extractStyle())
                }
            }
        }
    }

    fun getText(): String {
        return _textState.value.text
    }

    fun updateTextState(newValue: TextFieldValue) {
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

    fun updateCurrentStyle(updatedStyle: AnnotatedString.Range<SpanStyle>) {
        _currentStyle.value = updatedStyle
    }

    fun saveNote(doAfter: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDao.insertNote(
                Note(
                    id = if (id == null || id == -1) 0 else id!!,
                    title = _titleState.value,
                    textContent = buildAnnotatedString {
                        for (index in 0 until styleList.size) {
                            addStyle(
                                styleList[index].item,
                                styleList[index].start,
                                styleList[index].end
                            )
                        }
                        append(textState.value.text)
                    }.toHtml(),
                    attachments = jsonParser.toJson(_attachmentsList.value),
                    dateAdded = Date()
                )
            )
        }
        doAfter()
    }

}