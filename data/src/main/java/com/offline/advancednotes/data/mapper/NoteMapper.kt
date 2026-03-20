package com.offline.advancednotes.data.mapper

import com.offline.advancednotes.data.local.room.NoteEntity
import com.offline.advancednotes.domain.model.Note

/**
 * Mapper for converting between Domain Note and Room NoteEntity.
 */
object NoteMapper {

    fun Note.toEntity(): NoteEntity {
        return NoteEntity(
            id = id,
            title = title,
            content = content,
            tags = tags,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isDeleted = isDeleted,
            syncStatus = syncStatus
        )
    }

    fun NoteEntity.toDomain(): Note {
        return Note(
            id = id,
            title = title,
            content = content,
            tags = tags,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isDeleted = isDeleted,
            syncStatus = syncStatus
        )
    }
}