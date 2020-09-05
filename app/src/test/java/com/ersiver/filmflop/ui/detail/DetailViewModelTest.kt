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

        `when`(database.movieDao.getMovie(movie.id)).thenReturn(movie)
    }

    @Test
    fun getMovieTest() {
        viewModel.getMovie(movie.id)
        val value = viewModel.movie.getOrAwaitValue()
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
        viewModel.getMovie(movie.id)
        viewModel.movie.getOrAwaitValue()
        viewModel.onTrailerClicked()
        val event = viewModel.showTrailerEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(movie.trailerUrl))
    }

    @Test
    fun setFavouriteTest() {
        viewModel.getMovie(movie.id)
        val value = viewModel.movie.getOrAwaitValue()

        //Verify the initial isFavourite value is false
        assertThat(value.isFavourite, `is`(false))

        viewModel.setFavorite()
        assertThat(value.isFavourite, `is`(true))
    }
}