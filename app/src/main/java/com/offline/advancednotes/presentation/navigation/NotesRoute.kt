package com.offline.advancednotes.presentation.navigation

sealed class NotesRoute(val route: String) {
    data object List : NotesRoute("note_list")
    data object Editor : NotesRoute("note_editor/{${RouteArgs.NOTE_ID}}") {
        const val NEW_NOTE = "new"
        fun createRoute(noteId: String? = null) = "note_editor/${noteId ?: NEW_NOTE}"
    }
}