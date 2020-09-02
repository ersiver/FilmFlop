package com.ersiver.filmflop.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ersiver.filmflop.model.Genre
import com.ersiver.filmflop.model.Movie
import com.ersiver.filmflop.model.Trailer
import com.ersiver.filmflop.util.GenreConverter

@Database(
    entities = [Movie::class, Genre::class, Trailer::class], version = 1, exportSchema = false
)
@TypeConverters(GenreConverter::class)
abstract class FilmFlopDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
    abstract val genreDao: GenreDao
    abstract val trailerDao: TrailerDao
}