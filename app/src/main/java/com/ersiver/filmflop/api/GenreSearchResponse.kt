package com.ersiver.filmflop.api

import com.ersiver.filmflop.model.Genre
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class GenreSearchResponse(@Json(name = "genres") val apiGenres: List<ApiGenre>)

@JsonClass(generateAdapter = true)
data class ApiGenre(val id: Int, val name: String)


/**
 * Mapping data type returned from the API to database objects.
 */
fun GenreSearchResponse.asModel(): List<Genre> {
    return apiGenres.map {
        Genre(
            id = it.id,
            name = it.name
        )
    }
}