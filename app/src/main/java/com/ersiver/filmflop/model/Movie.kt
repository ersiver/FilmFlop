package com.ersiver.filmflop.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movies_table")
data class Movie(
    @PrimaryKey @ColumnInfo(name = "movie_id") val id: Int,
    @ColumnInfo(name = "movie_title") val title: String?,
    @ColumnInfo(name = "movie_overview") val overview: String?,
    @ColumnInfo(name = "movie_rating") val rating: Double?,
    @ColumnInfo(name = "movie_poster_url") val posterUrl: String?,
    @ColumnInfo(name = "movie_language") val language: String?,
    @ColumnInfo(name = "movie_release_date") val releaseDate: String?,
    @ColumnInfo(name = "movie_popularity") val popularity: Double?,
    @ColumnInfo(name = "movie_genres") val genreIds: List<Int>?,
    @ColumnInfo(name = "is_favourite") var isFavourite: Boolean = false,
    @ColumnInfo(name = "saved_date") var saveDate: Long? = null,
    @ColumnInfo(name = "genre_names") var genreNames: List<String>? = null,
    @ColumnInfo(name = "movie_trailer") var trailerUrl: String = ""
    ) : Parcelable