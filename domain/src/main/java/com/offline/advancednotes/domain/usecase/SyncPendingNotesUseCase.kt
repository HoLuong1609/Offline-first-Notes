package com.offline.advancednotes.domain.usecase

import com.offline.advancednotes.domain.model.SyncStatus
import com.offline.advancednotes.domain.repository.NotesRepository

class SyncPendingNotesUseCase(
    private val repository: NotesRepository
) {
    suspend operator fun invoke() {
        val pendingNotes = repository.getPendingSyncNotes()

        pendingNotes.forEach { localNote ->
            try {
                val remoteNote = repository.getRemoteNoteById(localNote.id)

                when {
                    // Case 1: No remote note or local is newer → Upload local (Last Write Wins)
                    remoteNote == null || localNote.updatedAt > remoteNote.updatedAt -> {
                        repository.saveNoteToRemote(localNote)
                        repository.updateSyncStatus(localNote.id, SyncStatus.SYNCED)
                    }

                    // Case 2: Remote is newer → Download and overwrite local
                    remoteNote.updatedAt > localNote.updatedAt -> {
                        repository.saveNote(remoteNote.copy(syncStatus = SyncStatus.SYNCED))
                        println("Conflict resolved: Remote version won for note ${localNote.id}")
                    }

                    // Case 3: Same timestamp → Already synced
                    else -> {
                        repository.updateSyncStatus(localNote.id, SyncStatus.SYNCED)
                    }
                }
            } catch (e: Exception) {
                println("Sync failed for note ${localNote.id}: ${e.message}")
                // Keep as PENDING for next retry
            }
        }
    }
}