package com.ersiver.filmflop.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres_table")
data class Genre(
    @PrimaryKey @ColumnInfo(name = "genre_id") val id: Int,
    @ColumnInfo(name = "genre_name") val name: String? = null
)