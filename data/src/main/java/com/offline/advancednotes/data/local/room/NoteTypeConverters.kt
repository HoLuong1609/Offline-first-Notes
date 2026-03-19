package com.offline.advancednotes.data.local.room

import androidx.room.TypeConverter
import com.offline.advancednotes.domain.model.SyncStatus

class NoteTypeConverters {

    @TypeConverter
    fun fromTags(tags: List<String>): String {
        return tags.joinToString(",")
    }

    @TypeConverter
    fun toTags(tagsString: String): List<String> {
        return if (tagsString.isEmpty()) emptyList() else tagsString.split(",")
    }

    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String = status.name

    @TypeConverter
    fun toSyncStatus(statusString: String): SyncStatus = SyncStatus.valueOf(statusString)
}