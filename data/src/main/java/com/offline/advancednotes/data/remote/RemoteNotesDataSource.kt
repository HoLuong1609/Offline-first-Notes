package com.offline.advancednotes.data.remote

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.offline.advancednotes.domain.model.Note
import kotlinx.coroutines.tasks.await

/**
 * Remote data source using Firebase Firestore.
 * Only handles individual note operations for sync.
 */
class RemoteNotesDataSource {

    companion object {
        private const val NOTES_COLLECTION = "notes"
    }

    private val firestore: FirebaseFirestore = Firebase.firestore
    private val notesCollection = firestore.collection(NOTES_COLLECTION)

    suspend fun getNoteById(id: String): Note? {
        val document = notesCollection.document(id).get().await()
        return if (document.exists()) {
            document.toObject(Note::class.java)
        } else {
            null
        }
    }

    suspend fun saveNote(note: Note) {
        notesCollection.document(note.id)
            .set(note)
            .await()
    }

    suspend fun deleteNote(id: String) {
        // Soft delete on server
        notesCollection.document(id)
            .update(
                mapOf(
                    "isDeleted" to true,
                    "updatedAt" to System.currentTimeMillis()
                )
            )
            .await()
    }
}