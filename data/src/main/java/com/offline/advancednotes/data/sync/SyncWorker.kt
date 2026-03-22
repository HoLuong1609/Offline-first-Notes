package com.offline.advancednotes.data.sync

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.offline.advancednotes.domain.usecase.SyncPendingNotesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncPendingNotesUseCase: SyncPendingNotesUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "SyncWorker started - syncing pending notes")

        return try {
            syncPendingNotesUseCase()
            Log.d(TAG, "SyncWorker completed successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "SyncWorker failed: ${e.message}")
            // Retry with exponential backoff (default WorkManager policy)
            Result.retry()
        }
    }

    companion object {
        const val TAG = "SyncWorker"
        const val UNIQUE_WORK_NAME = "notes_sync_work"
    }
}