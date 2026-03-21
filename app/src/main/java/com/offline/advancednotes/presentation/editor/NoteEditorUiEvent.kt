package com.offline.advancednotes.presentation.editor

sealed class NoteEditorUiEvent {
    data object NavigateBack : NoteEditorUiEvent()
    data class ShowError(val message: String) : NoteEditorUiEvent()
}
