package com.ersiver.filmflop

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ersiver.filmflop.db.GenreDao
import com.ersiver.filmflop.db.MovieDao
import com.ersiver.filmflop.model.Movie
import com.ersiver.filmflop.util.DEFAULT_SORT
import com.ersiver.filmflop.util.SortUtil
import com.ersiver.filmflop.utils.TestUtil
import com.ersiver.filmflop.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented  unit test, for implementation of [MovieDao].
 */
@SmallTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MovieDaoTest : LocalDatabase() {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var movieDao: MovieDao

    @Before
    fun setup() {
        movieDao = database.movieDao
    }

    @Test
    @Throws(Exception::class)
    fun insertAndLoadMovieById() = runBlockingTest {
        //Insert list containing a movie to db.
        val movie = TestUtil.createMovie()
        movieDao.insert(listOf(movie))

        //Get movie by id from the db.
        val loaded: Movie = movieDao.getMovie(movie.id)

        //Verify the loaded data contains the expected values.
        assertThat(loaded.id, `is`(movie.id))
        assertThat(loaded.title, `is`(movie.title))
        assertThat(loaded.overview, `is`(movie.overview))
        assertThat(loaded.rating, `is`(movie.rating))
        assertThat(loaded.posterUrl, `is`(movie.posterUrl))
        assertThat(loaded.language, `is`(movie.language))
        assertThat(loaded.releaseDate, `is`(movie.releaseDate))
        assertThat(loaded.popularity, `is`(movie.popularity))
        assertThat(loaded.genreIds, `is`(movie.genreIds))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndLoadList() = runBlockingTest {
        //Insert list to db. No favourite movies.
        val movie = TestUtil.createMovie()
        movieDao.insert(listOf(movie))

        //Get list from database.
        val sortType: SortUtil.Companion.MovieSortType =
            SortUtil.Companion.MovieSortType.valueOf(DEFAULT_SORT)
        val query = SortUtil.getAllQuery(sortType)
        val loaded = movieDao.getFavouriteMovies(query).getOrAwaitValue()

        //There are no favourite movies in the database.
        // Verify the loaded data is empty.
        assertThat(loaded.isEmpty(), `is` (true))
    }

    @Test
    @Throws(Exception::class)
    fun updateAndLoadFavourites() = runBlockingTest {
        //Insert list to database. Initially the isFavourite value is false.
        val movie = TestUtil.createMovie()
        movieDao.insert(listOf(movie))

        //Get list from database and verify the loaded data is empty.
        val sortType: SortUtil.Companion.MovieSortType =
            SortUtil.Companion.MovieSortType.valueOf(DEFAULT_SORT)
        val query = SortUtil.getAllQuery(sortType)
        var loaded = movieDao.getFavouriteMovies(query).getOrAwaitValue()
        assertThat(loaded.isEmpty(), `is` (true))

        //Change isFavourite value to true and update movie.
        movie.isFavourite = true
        movieDao.update(movie)

        //Load again. Verify there is 1 favourite movie in the database,
        // and contains the expected values.
        loaded = movieDao.getFavouriteMovies(query).getOrAwaitValue()
        assertThat(loaded.size, `is`(1))
        assertThat(loaded[0].id, `is`(movie.id))
        assertThat(loaded[0].title, `is`(movie.title))
        assertThat(loaded[0].overview, `is`(movie.overview))
        assertThat(loaded[0].rating, `is`(movie.rating))
        assertThat(loaded[0].posterUrl, `is`(movie.posterUrl))
        assertThat(loaded[0].language, `is`(movie.language))
        assertThat(loaded[0].releaseDate, `is`(movie.releaseDate))
        assertThat(loaded[0].popularity, `is`(movie.popularity))
        assertThat(loaded[0].genreIds, `is`(movie.genreIds))
    }

    @Test
    @Throws(Exception::class)
    fun insertAndLoadByTitle() = runBlockingTest {
        //Insert list to db. No favourite movies.
        val movie = TestUtil.createMovie()
        movieDao.insert(listOf(movie))

        //Get movie by id from the database by title.
        val loaded = movieDao.search(movie.title!!)

        //Verify the loaded data contains the expected values
        assertThat(loaded.size, `is`(1))
        assertThat(loaded[0].id, `is`(movie.id))
        assertThat(loaded[0].title, `is`(movie.title))
        assertThat(loaded[0].overview, `is`(movie.overview))
        assertThat(loaded[0].rating, `is`(movie.rating))
        assertThat(loaded[0].posterUrl, `is`(movie.posterUrl))
        assertThat(loaded[0].language, `is`(movie.language))
        assertThat(loaded[0].releaseDate, `is`(movie.releaseDate))
        assertThat(loaded[0].popularity, `is`(movie.popularity))
        assertThat(loaded[0].genreIds, `is`(movie.genreIds))
    }
}