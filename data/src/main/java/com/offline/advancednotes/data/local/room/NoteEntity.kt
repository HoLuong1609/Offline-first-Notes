package com.offline.advancednotes.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.offline.advancednotes.domain.model.SyncStatus

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val tags: List<String>,
    val createdAt: Long,
    val updatedAt: Long,
    val isDeleted: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)