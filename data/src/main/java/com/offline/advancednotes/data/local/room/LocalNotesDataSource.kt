package com.offline.advancednotes.data.local.room

import com.offline.advancednotes.data.mapper.NoteMapper.toDomain
import com.offline.advancednotes.data.mapper.NoteMapper.toEntity
import com.offline.advancednotes.domain.model.Note
import com.offline.advancednotes.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalNotesDataSource @Inject constructor(
    private val noteDao: NoteDao
) {

    fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getNoteById(id: String): Note? {
        return noteDao.getNoteById(id)?.toDomain()
    }

    suspend fun saveNote(note: Note) {
        val entity = note.toEntity()
        noteDao.insert(entity)
    }

    suspend fun deleteNote(id: String) {
        noteDao.softDelete(id)
    }

    fun searchNotes(query: String): Flow<List<Note>> {
        return noteDao.searchNotes(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getPendingSyncNotes(): List<Note> {
        return noteDao.getPendingSyncNotes().map { it.toDomain() }
    }

    suspend fun updateSyncStatus(id: String, status: SyncStatus) {
        noteDao.updateSyncStatus(id, status)
    }
}