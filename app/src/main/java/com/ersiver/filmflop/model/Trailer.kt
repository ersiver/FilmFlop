package com.ersiver.filmflop.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_trailer")
data class Trailer(
    @PrimaryKey
    @ColumnInfo(name = "movie_id") var movieId: Int,
    @ColumnInfo(name = "trailer_url") var url: String=""
)