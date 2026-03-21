package com.offline.advancednotes.core.util

fun formatRelativeTime(timestamp: Long): String {
    val minutes = (System.currentTimeMillis() - timestamp) / 60000
    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "$minutes min ago"
        minutes < 1440 -> "${minutes / 60}h ago"
        else -> "${minutes / 1440}d ago"
    }
}