package com.offline.advancednotes.data.di

import com.offline.advancednotes.domain.repository.NotesRepository
import com.offline.advancednotes.domain.usecase.SyncPendingNotesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideSyncPendingNotesUseCasee(
        repository: NotesRepository
    ): SyncPendingNotesUseCase {
        return SyncPendingNotesUseCase(repository)
    }
}
