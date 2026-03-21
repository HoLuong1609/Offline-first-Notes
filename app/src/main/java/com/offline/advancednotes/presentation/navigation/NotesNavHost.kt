package com.offline.advancednotes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.offline.advancednotes.presentation.editor.NoteEditorScreen
import com.offline.advancednotes.presentation.notes.NoteListScreen
import com.offline.advancednotes.presentation.notes.NotesViewModel

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
                onNoteClick = { note ->
                    navController.navigate(NotesRoute.Editor.createRoute(note.id))
                },
                onCreateNewNote = {
                    navController.navigate(NotesRoute.Editor.createRoute())
                }
            )
        }

        composable(
            route = NotesRoute.Editor.route,
            arguments = listOf(navArgument(RouteArgs.NOTE_ID) { type = NavType.StringType; nullable = true })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString(RouteArgs.NOTE_ID)
            NoteEditorScreen(
                navController = navController,
                noteId = noteId
            )
        }
    }
}