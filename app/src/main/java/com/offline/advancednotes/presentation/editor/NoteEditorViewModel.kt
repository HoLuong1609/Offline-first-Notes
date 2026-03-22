package com.offline.advancednotes.presentation.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.offline.advancednotes.data.sync.SyncManager
import com.offline.advancednotes.domain.model.Note
import com.offline.advancednotes.domain.repository.NotesRepository
import com.offline.advancednotes.presentation.navigation.NotesRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val syncManager: SyncManager
) : ViewModel() {

    private val _currentNote = MutableStateFlow<Note?>(null)
    val currentNote: StateFlow<Note?> = _currentNote.asStateFlow()

    private val _uiEvent = Channel<NoteEditorUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun loadNoteById(noteId: String?) {
        if (noteId == null || noteId == NotesRoute.Editor.NEW_NOTE) {
            _currentNote.value = null  // new note
            return
        }
        viewModelScope.launch {
            val note = repository.getNoteById(noteId)
            _currentNote.value = note
        }
    }

    fun onNoteSaved(note: Note) {
        viewModelScope.launch {
            try {
                repository.saveNote(note)
                syncManager.enqueueSync()
                _uiEvent.send(NoteEditorUiEvent.NavigateBack)
            } catch (e: Exception) {
                _uiEvent.send(NoteEditorUiEvent.ShowError("Failed to save note: ${e.message}"))
            }
        }
    }
}
