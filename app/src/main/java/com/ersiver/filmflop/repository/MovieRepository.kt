package com.ersiver.filmflop.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ersiver.filmflop.api.*
import com.ersiver.filmflop.db.FilmFlopDatabase
import com.ersiver.filmflop.model.Movie
import com.ersiver.filmflop.model.Trailer
import com.ersiver.filmflop.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MovieRepository @Inject constructor(
    val service: FilmFlopService,
    private val database: FilmFlopDatabase
) {

    val rateLimiter = RateLimiter<String>(10, TimeUnit.MINUTES)

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean>
        get() = _isFavourite

    /**
     * Room and network operations to get list of searched movies
     * from the network and insert to database. Use a coroutine
     * suspend functions to run in a background thread.
     */
    suspend fun searchMovies(query: String): LiveData<Resource<List<Movie>>> {
        return object : NetworkBoundResource<List<Movie>, List<ApiMovie>>() {

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return data.isNullOrEmpty() || rateLimiter.shouldFetch(query)
            }

            override suspend fun loadFromDb(): List<Movie> {
                return withContext(Dispatchers.IO) {
                    database.movieDao.search(query)
                }
            }

            override fun processResponse(response: List<ApiMovie>): List<Movie> {
                return response.asModel()
            }

            override suspend fun saveCallResults(items: List<Movie>) {
                for (item in items) {
                    item.genreNames = item.genreIds?.let { getGenresNames(it) }
                    item.trailerUrl = getTrailer(item.id)
                }

                withContext(Dispatchers.IO) {
                    database.movieDao.insert(items)
                }
            }

            override fun onFetchFailed() {
                rateLimiter.reset(query)
            }

            override suspend fun createCallAsync(): List<ApiMovie> {
                return withContext(Dispatchers.IO) {
                    val response = service.getMoviesAsync(title = query).await()
                    response.apiMovies
                }
            }

        }.build().asLiveData()
    }

    /**
     * Room operation. Updates value of [Movie.isFavourite].
     * Uses a coroutine suspend function to run in a background thread.
     */
    suspend fun update(movie: Movie) {
        withContext(Dispatchers.IO) {
            database.movieDao.update(movie)
            _isFavourite.postValue(movie.isFavourite)
        }
    }

    /**
     * Room query operation. Returns instance of [Movie]
     * of the given [id]. Uses a coroutine suspend function
     * to run in a background thread.
     */
    suspend fun getMovie(id: Int) {
        withContext(Dispatchers.IO) {
            val movie = database.movieDao.getMovie(id)
            _movie.postValue(movie)
            _isFavourite.postValue(movie.isFavourite)
        }
    }

    /**
     * Room query operation. Returns list of favourite
     * movies wrapped in LiveData. Automatically runs
     * the query asynchronously on a background thread.
     */
    fun getFavouriteMovies(sort: String): LiveData<List<Movie>> {
        val sortType: SortUtil.Companion.MovieSortType =
            SortUtil.Companion.MovieSortType.valueOf(sort)
        val query = SortUtil.getAllQuery(sortType)
        return database.movieDao.getFavouriteMovies(query)
    }


    /**
     * Get movie's genres from the database.
     * If the database is null or empty get from network.
     */
    private suspend fun getGenresNames(ids: List<Int>): List<String> {
        return withContext(Dispatchers.IO) {
            if (shouldFetch(database.genreDao.getGenres(ids)))
                getGenresFromNetwork()
            database.genreDao.getGenres(ids)
        }
    }

    /**
     * Network and room operations.
     *
     * Get a list of genres from the network and insert to database.
     * Use a coroutine suspend functions to run in a background thread.
     */
    private suspend fun getGenresFromNetwork() {
        withContext(Dispatchers.IO) {
            val genresDeferred = service.getGenresAsync().await()
            database.genreDao.insertAll(genresDeferred.asModel())
        }
    }

    /**
     * Check whether we should fetch genre data from the network or not.
     */
    private fun shouldFetch(data: List<String>?): Boolean {
        return data.isNullOrEmpty() || rateLimiter.shouldFetch("genreKey")
    }

    /**
     * Get movie's trailer from the database.
     * If the trailers is null get from network.
     */
    suspend fun getTrailer(id: Int): String {
        return withContext(Dispatchers.IO) {
            if (shouldFetch(database.trailerDao.getTrailer(id), id.toString()))
                getTrailerFromNetwork(id)

            database.trailerDao.getTrailer(id).url
        }
    }

    /**
     * Network and room operations.
     *
     * Get the trailer from network and insert to database.
     * Use a coroutine suspend functions to run in a background thread.
     */
    private suspend fun getTrailerFromNetwork(id: Int) {
        withContext(Dispatchers.IO) {
            val response = service.getTrailerAsync(id).await()
            val trailerList = response.trailersApi

            val trailer = Trailer(id)

            if (trailerList.isNotEmpty())
                trailerList[0].url?.let {
                    trailer.url = TRAILER_URL + it
                }

            database.trailerDao.insert(trailer)
        }
    }

    /**
     * Check whether we should fetch trailer from the network or not.
     */
    private fun shouldFetch(trailer: Trailer?, key: String): Boolean {
        return trailer == null || rateLimiter.shouldFetch(key)
    }
}