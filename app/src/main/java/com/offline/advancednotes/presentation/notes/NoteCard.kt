package com.offline.advancednotes.presentation.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.offline.advancednotes.R
import com.offline.advancednotes.core.util.formatRelativeTime
import com.offline.advancednotes.domain.model.Note
import com.offline.advancednotes.domain.model.SyncStatus
import com.offline.advancednotes.ui.theme.AdvancedNotesAppTheme

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = note.title.ifBlank { stringResource(R.string.untitled) },
                    style = MaterialTheme.typography.titleMedium
                )

                if (note.syncStatus == SyncStatus.PENDING) {
                    Icon(
                        Icons.Default.CloudUpload,
                        contentDescription = stringResource(R.string.syncing),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = note.content.take(80) + if (note.content.length > 80) "..." else "",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (note.tags.isNotEmpty()) {
                Row {
                    note.tags.take(3).forEach { tag ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(tag) },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.updated_note_msg, formatRelativeTime(note.updatedAt)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Preview for single NoteCard
@Preview(showBackground = true, name = "Note Card - With Tags & Pending Sync")
@Composable
fun NoteCardWithTagsAndPendingPreview() {
    AdvancedNotesAppTheme {
        NoteCard(
            note = Note(
                id = "preview1",
                title = "Project Planning",
                content = "Review requirements and create task list for Q2. Include UI/UX improvements and backend optimization.",
                tags = listOf("work", "planning", "important"),
                createdAt = System.currentTimeMillis() - 7200000, // 2 hours ago
                updatedAt = System.currentTimeMillis() - 3600000, // 1 hour ago
                isDeleted = false,
                syncStatus = SyncStatus.PENDING
            ),
            onClick = {}
        )
    }
}

// Preview for NoteCard without tags
@Preview(showBackground = true, name = "Note Card - No Tags")
@Composable
fun NoteCardNoTagsPreview() {
    AdvancedNotesAppTheme {
        NoteCard(
            note = Note(
                id = "preview2",
                title = "Shopping Reminder",
                content = "Buy new headphones and check for discounts on Amazon.",
                tags = emptyList(),
                createdAt = System.currentTimeMillis() - 86400000, // 1 day ago
                updatedAt = System.currentTimeMillis() - 43200000, // 12 hours ago
                isDeleted = false,
                syncStatus = SyncStatus.SYNCED
            ),
            onClick = {}
        )
    }
}