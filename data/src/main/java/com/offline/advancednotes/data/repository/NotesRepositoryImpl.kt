package com.offline.advancednotes.data.repository

import com.offline.advancednotes.data.local.room.LocalNotesDataSource
import com.offline.advancednotes.data.remote.RemoteNotesDataSource
import com.offline.advancednotes.domain.model.Note
import com.offline.advancednotes.domain.model.SyncStatus
import com.offline.advancednotes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotesRepositoryImpl @Inject constructor(
    private val localDataSource: LocalNotesDataSource,
    private val remoteDataSource: RemoteNotesDataSource
) : NotesRepository {

    override fun getAllNotes(): Flow<List<Note>> {
        return localDataSource.getAllNotes()
    }

    override suspend fun getNoteById(id: String): Note? {
        return localDataSource.getNoteById(id)
    }

    override suspend fun saveNote(note: Note) {
        localDataSource.saveNote(note.copy(
            updatedAt = System.currentTimeMillis(),
            syncStatus = SyncStatus.PENDING
        ))
    }

    override suspend fun deleteNote(id: String) {
        localDataSource.deleteNote(id)
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return localDataSource.searchNotes(query)
    }

    override suspend fun getPendingSyncNotes(): List<Note> =
        localDataSource.getPendingSyncNotes()

    override suspend fun updateSyncStatus(id: String, status: SyncStatus) =
        localDataSource.updateSyncStatus(id, status)

    override suspend fun getRemoteNoteById(id: String): Note? =
        remoteDataSource.getNoteById(id)

    override suspend fun saveNoteToRemote(note: Note) =
        remoteDataSource.saveNote(note)
}