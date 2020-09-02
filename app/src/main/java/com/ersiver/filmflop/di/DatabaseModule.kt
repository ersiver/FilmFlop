package com.ersiver.filmflop.di

import android.app.Application
import androidx.room.Room
import com.ersiver.filmflop.db.FilmFlopDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): FilmFlopDatabase {
        return Room
            .databaseBuilder(app, FilmFlopDatabase::class.java, "filmFlop_dp")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(db: FilmFlopDatabase) = db.movieDao

    @Provides
    @Singleton
    fun provideGenreDao(db: FilmFlopDatabase) = db.genreDao


    @Provides
    @Singleton
    fun provideTrailerDao(db: FilmFlopDatabase) = db.trailerDao
}