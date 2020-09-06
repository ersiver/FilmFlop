package com.ersiver.filmflop.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ersiver.filmflop.MainCoroutinesRule
import com.ersiver.filmflop.api.FilmFlopService
import com.ersiver.filmflop.db.FilmFlopDatabase
import com.ersiver.filmflop.repository.MovieRepository
import com.ersiver.filmflop.utils.TestUtil
import com.ersiver.filmflop.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Local unit tests for the implementation of [DetailViewModel]
 */

@ExperimentalCoroutinesApi
class DetailViewModelTest {
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailViewModel
    private lateinit var repository: MovieRepository
    private var database: FilmFlopDatabase =
        mock(FilmFlopDatabase::class.java, Mockito.RETURNS_DEEP_STUBS)
    private var service: FilmFlopService = mock(FilmFlopService::class.java)
    private val movie = TestUtil.createMovie()

    @Before
    fun setUp() = runBlockingTest {
        repository = MovieRepository(service, database)
        viewModel = DetailViewModel(repository)

        //Load the movie from database for each test.
        `when`(database.movieDao.getMovie(movie.id)).thenReturn(movie)
    }

    @Test
    fun getMovieTest() {
        //When ViewModel is asked to load a movie by id
        viewModel.getMovie(movie.id)
        val value = viewModel.movie.getOrAwaitValue()

        //Then the observed data matches the expected values.
        assertThat(value.id, `is`(movie.id))
        assertThat(value.title, `is`(movie.title))
        assertThat(value.overview, `is`(movie.overview))
        assertThat(value.rating, `is`(movie.rating))
        assertThat(value.posterUrl, `is`(movie.posterUrl))
        assertThat(value.language, `is`(movie.language))
        assertThat(value.releaseDate, `is`(movie.releaseDate))
        assertThat(value.popularity, `is`(movie.popularity))
        assertThat(value.genreIds, `is`(movie.genreIds))
        assertThat(value.isFavourite, `is`(movie.isFavourite))
        assertThat(value.saveDate, `is`(movie.saveDate))
        assertThat(value.genreNames, `is`(movie.genreNames))
    }

    @Test
    fun onTrailerClickedTest() {
        //Load the movie
        viewModel.getMovie(movie.id)
        viewModel.movie.getOrAwaitValue()

        // When the ViewModel is asked to navigate to trailer.
        viewModel.onTrailerClicked()

        //Then the value of trailer event is a movie.
        val event = viewModel.showTrailerEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(movie.trailerUrl))
    }

    @Test
    fun setFavouriteTest() {
        //Load the movie
        viewModel.getMovie(movie.id)
        val value = viewModel.movie.getOrAwaitValue()

        //Verify the initial isFavourite value is false
        assertThat(value.isFavourite, `is`(false))

        //When viewModel is asked to change the isFavourite value.
        viewModel.setFavorite()

        //Then the new value is true.
        assertThat(value.isFavourite, `is`(true))
    }
}