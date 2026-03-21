package com.offline.advancednotes.presentation.navigation

sealed class NotesRoute(val route: String) {
    data object List : NotesRoute("note_list")
}