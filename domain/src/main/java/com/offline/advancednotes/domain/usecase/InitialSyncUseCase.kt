package com.offline.advancednotes.domain.usecase

import com.offline.advancednotes.domain.model.SyncStatus
import com.offline.advancednotes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.first

class InitialSyncUseCase(
    private val repository: NotesRepository
) {
    suspend operator fun invoke() {
        val localNotes = repository.getAllNotes().first()
        if (localNotes.isEmpty()) {
            println("Local database is empty → start initial pull from Firestore")
            try {
                val remoteNotes = repository.getRemoteNotes()
                remoteNotes.forEach { note ->
                    // save to local, mark SYNCED
                    repository.saveNote(note.copy(syncStatus = SyncStatus.SYNCED))
                }
                println("Initial pull complete: ${remoteNotes.size} note downloaded")
            } catch (e: Exception) {
                println("Initial pull failed: ${e.message}")
            }
        } else {
            println("Local already has notes → no need initial pull")
        }
    }
}