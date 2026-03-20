package com.offline.advancednotes.data.di

import com.offline.advancednotes.data.repository.NotesRepositoryImpl
import com.offline.advancednotes.domain.repository.NotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindKlineRepository(
        impl: NotesRepositoryImpl
    ): NotesRepository
}