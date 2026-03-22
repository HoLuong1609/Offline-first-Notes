package com.offline.advancednotes.domain.repository

import com.offline.advancednotes.domain.model.Note
import com.offline.advancednotes.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: String): Note?
    suspend fun saveNote(note: Note)
    suspend fun deleteNote(id: String)
    fun searchNotes(query: String): Flow<List<Note>>

    suspend fun getPendingSyncNotes(): List<Note>
    suspend fun updateSyncStatus(id: String, status: SyncStatus)

    suspend fun getRemoteNoteById(id: String): Note?
    suspend fun saveNoteToRemote(note: Note)
}