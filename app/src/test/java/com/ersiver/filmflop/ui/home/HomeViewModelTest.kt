package com.ersiver.filmflop.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ersiver.filmflop.MainCoroutinesRule
import com.ersiver.filmflop.model.Movie
import com.ersiver.filmflop.repository.MovieRepository
import com.ersiver.filmflop.utils.TestUtil
import com.ersiver.filmflop.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

/**
 * Local unit tests for the implementation of [HomeViewModel]
 */

@ExperimentalCoroutinesApi
class HomeViewModelTest {
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: MovieRepository
    private lateinit var movie: Movie

    @Before
    fun setUp() {
        repository = Mockito.mock(MovieRepository::class.java)
        viewModel = HomeViewModel(repository)
        movie = TestUtil.createMovie()
    }

    @Test
    fun startTest() {
        viewModel.start("sortByTitle", 2)
        val sortData = viewModel.sortType.getOrAwaitValue()
        assertThat(sortData, `is`("sortByTitle"))
        val columnData = viewModel.columnCount.getOrAwaitValue()
        assertThat(columnData, `is`(2))
    }

    @Test
    fun navigateToSearchTest() {
        viewModel.navigateToSearch()
        val event = viewModel.navigateToSearch.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(true))
    }

    @Test
    fun navigateToDetailTest() {
        viewModel.navigateToDetail(movie)
        val event = viewModel.navigateToDetail.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(movie))
    }

    @Test
    fun displayMenuWithDeleteTest() {
        viewModel.displayMenuWithDelete(movie)
        val event = viewModel.contextualMenuEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(movie))
    }

    @Test
    fun removeFromFavouriteTest(){
        // Set isFavourite value to true.
        movie.isFavourite = true

        // When the ViewModel is asked to remove from favourite
        viewModel.removeFromFavourite(movie)

        //Verify that movie.isFavourite value is now false.
        assertThat(movie.isFavourite, `is`(false))
    }

    @Test
    fun displaySnackBarWithUndoTest() {
        viewModel.displaySnackBarWithUndo(movie)
        val event = viewModel.snackBarEvent.getOrAwaitValue()
        assertThat(event.getContentIfNotHandled(), `is`(movie))
    }

    @Test
    fun undoRemoveTest() {
        viewModel.removeFromFavourite(movie)
        assertThat(movie.isFavourite, `is`(false))
        viewModel.undoRemove(movie)
        assertThat(movie.isFavourite, `is`(true))
    }
}