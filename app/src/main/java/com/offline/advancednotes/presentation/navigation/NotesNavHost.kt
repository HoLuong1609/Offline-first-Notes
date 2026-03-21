package com.offline.advancednotes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.offline.advancednotes.presentation.notes.NotesViewModel
import com.offline.advancednotes.presentation.notes.NoteListScreen

@Composable
fun NotesNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NotesRoute.List.route
    ) {
        composable(NotesRoute.List.route) { backStackEntry ->
            val viewModel: NotesViewModel = hiltViewModel(backStackEntry)  // scope theo nav entry
            NoteListScreen(
                viewModel = viewModel,
                onNoteClick = {},
                onCreateNewNote = {}
            )
        }
    }
}