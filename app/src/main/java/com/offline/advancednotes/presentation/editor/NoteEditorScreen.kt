package com.offline.advancednotes.presentation.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.offline.advancednotes.R
import com.offline.advancednotes.domain.model.Note
import com.offline.advancednotes.presentation.navigation.NotesRoute
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    navController: NavController,
    noteId: String?,
    viewModel: NoteEditorViewModel = hiltViewModel()
) {
    val currentNote by viewModel.currentNote.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    // Input state
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var tagsInput by remember { mutableStateOf("") }

    // Load note data
    LaunchedEffect(noteId) {
        viewModel.loadNoteById(noteId)
    }

    // Update state when note data is loaded from ViewModel
    LaunchedEffect(currentNote) {
        currentNote?.let {
            title = it.title
            content = it.content
            tagsInput = it.tags.joinToString(", ")
        }
    }

    // Listen for UI events according to Lifecycle
    LaunchedEffect(viewModel.uiEvent, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is NoteEditorUiEvent.NavigateBack -> navController.popBackStack()
                    is NoteEditorUiEvent.ShowError -> {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (noteId == null || noteId == NotesRoute.Editor.NEW_NOTE) stringResource(
                            R.string.new_note
                        ) else stringResource(
                            R.string.edit_note
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(
                            R.string.back
                        ))
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.title)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                singleLine = true
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(stringResource(R.string.content)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                maxLines = 10
            )

            OutlinedTextField(
                value = tagsInput,
                onValueChange = { tagsInput = it },
                label = { Text(stringResource(R.string.tags_msg)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.tags_hint)) }
            )

            Button(
                onClick = {
                    if (title.trim().isEmpty()) {
                        return@Button
                    }

                    val tags = tagsInput.split(",")
                        .map { it.trim() }
                        .filter { it.isNotBlank() }

                    val noteToSave = Note(
                        id = currentNote?.id ?: UUID.randomUUID().toString(),
                        title = title.trim(),
                        content = content.trim(),
                        tags = tags,
                        createdAt = currentNote?.createdAt ?: System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )

                    viewModel.onNoteSaved(noteToSave)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(stringResource(R.string.save_note))
            }
        }
    }
}