package com.offline.advancednotes.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.offline.advancednotes.domain.model.Note
import com.offline.advancednotes.domain.repository.NotesRepository
import com.offline.advancednotes.domain.usecase.InitialSyncUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val initialSyncUseCase: InitialSyncUseCase
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        observeNotes()
        syncInitialData()
    }

    private fun observeNotes() {
        repository.getAllNotes()
            .onEach { _notes.value = it }
            .launchIn(viewModelScope)
    }

    /**
     * Initial sync when launch app (if local database is empty).
     */
    private fun syncInitialData() {
        viewModelScope.launch {
            initialSyncUseCase()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            observeNotes()
        } else {
            repository.searchNotes(query)
                .onEach { _notes.value = it }
                .launchIn(viewModelScope)
        }
    }
}