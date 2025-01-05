package com.example.keepall.bottombar

sealed class BottomBarState(
    val isDialogVisible: Boolean,
    val dialogType: BottomBarDialogType,

    ) {
    class NoteMenu(
        isDialogVisible: Boolean = false,
        dialogType: BottomBarDialogType = BottomBarDialogType.NO_DIALOG
    ) :
        BottomBarState(isDialogVisible, dialogType)

    class TextMenu(
        isDialogVisible: Boolean = false,
        dialogType: BottomBarDialogType = BottomBarDialogType.NO_DIALOG
    ) :
        BottomBarState(isDialogVisible, dialogType)

}

fun BottomBarState.copy(
    isDialogVisible: Boolean? = null,
    dialogType: BottomBarDialogType? = null
): BottomBarState {

    return when (this) {
        is BottomBarState.NoteMenu -> BottomBarState.NoteMenu(
            isDialogVisible ?: this.isDialogVisible, dialogType ?: this.dialogType
        )

        is BottomBarState.TextMenu -> BottomBarState.TextMenu(
            isDialogVisible ?: this.isDialogVisible, dialogType ?: this.dialogType
        )
    }
}