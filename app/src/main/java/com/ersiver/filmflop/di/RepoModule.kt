package com.ersiver.filmflop.di

import com.ersiver.filmflop.api.FilmFlopService
import com.ersiver.filmflop.db.FilmFlopDatabase
import com.ersiver.filmflop.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideRepository(service: FilmFlopService, db: FilmFlopDatabase) =
        MovieRepository(service, db)
}