package com.ersiver.filmflop.api

import com.ersiver.filmflop.util.API_KEY
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmFlopService{
    @GET("search/movie")
    fun getMoviesAsync(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("query") title: String
    ): Deferred<MovieSearchResponse>


    @GET("genre/movie/list")
    fun getGenresAsync(
        @Query("api_key") apiKey: String = API_KEY
    ): Deferred<GenreSearchResponse>


    @GET("movie/{movie_id}/videos")
    fun getTrailerAsync(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = API_KEY
    ) : Deferred<TrailerSearchResponse>
}