package com.example.keepall.screens.notescreen

import android.text.ParcelableSpan
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepall.bottombar.BottomBarDialogType
import com.example.keepall.bottombar.BottomBarState
import com.example.keepall.bottombar.copy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch


class NewNoteScreenViewModel : ViewModel() {

    val titleSpans = arrayOf<ParcelableSpan>()
    val noteSpans = arrayOf<ParcelableSpan>()

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

}