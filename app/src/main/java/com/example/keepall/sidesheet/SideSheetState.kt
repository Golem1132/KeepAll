package com.example.keepall.sidesheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

enum class SideSheetValue{
    OPENED, CLOSED
}

class SideSheetState(initValue: SideSheetValue, confirmStateChange: () -> Boolean = {true}) {
    var currentValue: MutableState<SideSheetValue> = mutableStateOf(initValue)


    val isClosed = currentValue.value == SideSheetValue.CLOSED
    val isOpened = currentValue.value == SideSheetValue.OPENED

    fun open() {
        currentValue.value = SideSheetValue.OPENED
    }

    fun close() {
        currentValue.value = SideSheetValue.CLOSED
    }


    companion object {
        fun saver(confirmStateChange: () -> Boolean) =
            Saver<SideSheetState, SideSheetValue>(
                save = {it.currentValue.value},
                restore = { SideSheetState(it, confirmStateChange) }
            )
    }
}

@Composable
fun rememberSideSheetState(
    initValue: SideSheetValue,
    confirmStateChange: () -> Boolean = {true}
): SideSheetState {
    return rememberSaveable(saver = SideSheetState.saver(confirmStateChange)) {
        SideSheetState(initValue, confirmStateChange)
    }
}