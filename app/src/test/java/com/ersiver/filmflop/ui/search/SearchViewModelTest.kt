package com.ersiver.filmflop.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ersiver.filmflop.MainCoroutinesRule
import com.ersiver.filmflop.model.Movie
import com.ersiver.filmflop.repository.MovieRepository
import com.ersiver.filmflop.util.Resource
import com.ersiver.filmflop.utils.TestUtil
import com.ersiver.filmflop.utils.getOrAwaitValue
import com.ersiver.filmflop.utils.mock
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
import org.mockito.Mockito.verify

/**
 * Local unit tests for the implementation of [SearchViewModel]
 */
@ExperimentalCoroutinesApi
class SearchViewModelTest {
    @get:Rule
    val mainCoroutinesRule = MainCoroutinesRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchViewModel
    private lateinit var repository: MovieRepository
    private lateinit var movie: Movie

    @Before
    fun setUp() {
        repository = mock(MovieRepository::class.java, Mockito.RETURNS_DEEP_STUBS)
        viewModel = SearchViewModel(repository)
        movie = TestUtil.createMovie()
    }

    @Test
    fun startTest() {
        viewModel.start(2)
        val value = viewModel.columnCount.getOrAwaitValue()
        assertThat(value, `is`(2))
    }

    @Test
    fun updateGridLayoutTest() {
        viewModel.start(1)
        var value = viewModel.columnCount.getOrAwaitValue()
        assertThat(value, `is`(1))

        viewModel.updateGridLayout()
        value = viewModel.columnCount.getOrAwaitValue()
        assertThat(value, `is`(2))
    }

    @Test
    fun navigateToDetailTest() {
        viewModel.navigateToDetail(movie)
        val event = viewModel.navigateToDetailEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(movie))
    }

    @Test
    fun searchTest() = runBlockingTest {
        val foo = MutableLiveData<Resource<List<Movie>>>()
        `when`(repository.searchMovies("foo")).thenReturn(foo)
        val observer = mock<Observer<Resource<List<Movie>>>>()
        viewModel.results.observeForever(observer)
        viewModel.search("foo")
        verify(repository).searchMovies("foo")
    }
}