package com.ersiver.filmflop.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ersiver.filmflop.MainCoroutinesRule
import com.ersiver.filmflop.api.FilmFlopService
import com.ersiver.filmflop.db.FilmFlopDatabase
import com.ersiver.filmflop.db.GenreDao
import com.ersiver.filmflop.db.MovieDao
import com.ersiver.filmflop.db.TrailerDao
import com.ersiver.filmflop.model.Movie
import com.ersiver.filmflop.utils.TestUtil
import com.ersiver.filmflop.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

/**
 * Local unit tests for the implementation of [MovieRepository]
 */
@ExperimentalCoroutinesApi
class MovieRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutinesRule = MainCoroutinesRule()

    private lateinit var repository: MovieRepository
    private val service = mock(FilmFlopService::class.java)
    private lateinit var database: FilmFlopDatabase

    @Test
    fun loadMoviesFromDbTest() = runBlockingTest {
        database = mock(FilmFlopDatabase::class.java)
        repository = MovieRepository(service, database)

       val movieDao = mock(MovieDao::class.java)
        `when`(database.movieDao).thenReturn(movieDao)

        //Data loaded from database is not null or empty
        val data = listOf(TestUtil.createMovie())
        `when`(movieDao.getMovies("foo")).thenReturn(data)

        //Act
        repository.searchMovies("foo").getOrAwaitValue()


        //Assert invocation of movieDao.getMovies() on certain query
        verify(movieDao, atLeastOnce()).getMovies("foo")
    }

    @Test
    fun fetchMoviesFromNetwork() = runBlockingTest {
        database = mock(FilmFlopDatabase::class.java)
        repository = MovieRepository(service, database)

        val movieDao = mock(MovieDao::class.java)
        `when`(database.movieDao).thenReturn(movieDao)

        //Data loaded from database is null
        `when`(movieDao.getMovies("bar")).thenReturn(emptyList())

        //Act
        repository.searchMovies("bar").getOrAwaitValue()

        //Assert invocation of service.getMoviesAsync() on certain query.
        verify(service).getMoviesAsync(title = "bar")
    }
}