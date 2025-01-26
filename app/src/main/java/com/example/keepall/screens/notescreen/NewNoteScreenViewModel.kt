package com.example.keepall.screens.notescreen

import android.text.ParcelableSpan
import android.text.SpannableString
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepall.bottombar.BottomBarDialogType
import com.example.keepall.bottombar.BottomBarState
import com.example.keepall.bottombar.copy
import com.example.keepall.model.NoteTextStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class NewNoteScreenViewModel : ViewModel() {

    private val _currentStyle = MutableStateFlow<NoteTextStyle>(NoteTextStyle())
    val currentStyle = _currentStyle.asStateFlow()

    private val _noteTextFieldValue = MutableStateFlow<TextFieldValue>(TextFieldValue())
    val noteTextFieldValue = _noteTextFieldValue

    val styledText = mutableListOf<NoteTextStyle>()

    private val _bottomBarState = MutableStateFlow<BottomBarState>(
        BottomBarState.NoteMenu(
            false,
            BottomBarDialogType.NO_DIALOG
        )
    )
    val bottomBarState = _bottomBarState.asStateFlow()


    fun toggleDialogVisibility(type: BottomBarDialogType? = null) {
        viewModelScope.launch {
            _bottomBarState.emit(
                _bottomBarState.value.copy(
                    !_bottomBarState.value.isDialogVisible,
                    type
                )
            )
        }
    }

    fun setMenuType(menu: BottomBarState) {
        viewModelScope.launch {
            _bottomBarState.emit(
                menu
            )
        }
    }

    fun updateNoteTextField(newField: TextFieldValue) {
        viewModelScope.launch {
            when {
                newField.text.length > _noteTextFieldValue.value.text.length -> {
                    styledText.add(_noteTextFieldValue.value.selection.end, _currentStyle.value)
                }

                newField.text.length < _noteTextFieldValue.value.text.length -> {
                    if (_noteTextFieldValue.value.selection.length > 0) {
                        var last = _noteTextFieldValue.value.selection.end - 1
                        for (i in 0 until _noteTextFieldValue.value.selection.length) {
                            styledText.removeAt(last)
                            last--
                        }
                    } else
                        styledText.removeAt(newField.selection.end)
                }
            }
            _noteTextFieldValue.emit(newField)
        }
    }

    fun setBold(isBold: Boolean) {
        viewModelScope.launch {
            _currentStyle.emit(_currentStyle.value.copy(bold = isBold))
        }
    }

    fun setUnderline(isUnderlined: Boolean) {
        viewModelScope.launch {
            _currentStyle.emit(_currentStyle.value.copy(underline = isUnderlined))
        }
    }

    fun setItalic(isItalic: Boolean) {
        viewModelScope.launch {
            _currentStyle.emit(_currentStyle.value.copy(italic = isItalic))
        }
    }

    fun setFontSize(sizeMultiplier: Float) {
        viewModelScope.launch {
            _currentStyle.emit(_currentStyle.value.copy(fontSize = sizeMultiplier))
        }
    }


    fun clearStyle() {
        viewModelScope.launch {
            _currentStyle.emit(NoteTextStyle())
        }
    }

}