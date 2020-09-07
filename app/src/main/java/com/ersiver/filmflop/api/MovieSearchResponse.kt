package com.ersiver.filmflop.api

import com.ersiver.filmflop.model.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieSearchResponse(
    @Json(name = "results")
    val apiMovies: List<ApiMovie>
)

@JsonClass(generateAdapter = true)
data class ApiMovie(
    val id: Int,
    val title: String?,
    val overview: String,
    val popularity: Double?,
    @Json(name = "vote_average") val rating: Double?,
    @Json(name = "poster_path") val posterUrl: String?,
    @Json(name = "original_language") val language: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "genre_ids") val genreIds: List<Int>?
)

/**
 * Mapping data type returned from the API to database objects.
 */
fun List<ApiMovie>.asModel(): List<Movie> {
    return map {
        Movie(
            id = it.id,
            title = it.title,
            overview = it.overview,
            rating = it.rating,
            posterUrl = it.posterUrl,
            language = it.language,
            releaseDate = it.releaseDate ,
            popularity = it.popularity,
            genreIds = it.genreIds
        )
    }
}