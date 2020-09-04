package com.ersiver.filmflop.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ersiver.filmflop.model.Movie
import com.ersiver.filmflop.repository.MovieRepository
import com.ersiver.filmflop.util.DEFAULT_SORT
import com.ersiver.filmflop.util.SingleEvent
import com.ersiver.filmflop.util.TITLE_SORT
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private var movieSource: LiveData<List<Movie>> = MutableLiveData()
    private val _favouriteMovies = MediatorLiveData<List<Movie>>()
    val favouriteMovies: LiveData<List<Movie>> = _favouriteMovies

    private val _sortType = MutableLiveData<String>()
    val sortType: LiveData<String> = _sortType

    private val _columnCount = MutableLiveData<Int>()
    val columnCount: LiveData<Int> = _columnCount

    private val _navigateToDetail = MutableLiveData<SingleEvent<Movie>>()
    val navigateToDetail: LiveData<SingleEvent<Movie>> = _navigateToDetail

    private val _navigateToSearch = MutableLiveData<SingleEvent<Boolean>>()
    val navigateToSearch: LiveData<SingleEvent<Boolean>> = _navigateToSearch

    private val _snackBarEvent = MutableLiveData<SingleEvent<Movie>>()
    val snackBarEvent: LiveData<SingleEvent<Movie>> = _snackBarEvent

    private val _contextualMenuEvent = MutableLiveData<SingleEvent<Movie>>()
    val contextualMenuEvent: LiveData<SingleEvent<Movie>> = _contextualMenuEvent


    /**
     * Called in HomeFragment onActivityCreated.
     */
    fun start(currentSort: String, columnCountGrid: Int) {
        _columnCount.value = columnCountGrid
        _sortType.value = currentSort
    }

    /**
     * Executes whenever ImageButton action_sort is clicked.
     */
    fun updateSortType() {
        val newSortType = if (_sortType.value == DEFAULT_SORT) TITLE_SORT else DEFAULT_SORT
        _sortType.value = newSortType
    }

    /**
     * Submits the list of favourite movies via BindingAdapter.
     */
    fun getFavourites(sort: String) {
        _favouriteMovies.removeSource(movieSource)
        movieSource = repository.getFavouriteMovies(sort)
        _favouriteMovies.addSource(movieSource) {
            _favouriteMovies.value = it
        }
    }

    /**
     * Executes once the ImageButton action_layout is clicked.
     * Gets the column count and sets the appropriate span count
     * and the icon of layout_action ImageButton via BindingAdapter.
     */
    fun updateGridLayout() {
        val newColumnCount = if (_columnCount.value == 1) 2 else 1
        _columnCount.value = newColumnCount
    }

    /**
     * Called once the ImageButton nav_search is clicked.
     * Triggers navigation to the SearchFragment.
     */
    fun navigateToSearch() {
        _navigateToSearch.value = SingleEvent(true)
    }

    /**
     * Executes onClick(), when a [Movie] is selected.
     * Triggers navigation to DetailFragment.
     */
    fun navigateToDetail(movie: Movie) {
        _navigateToDetail.value = SingleEvent(movie)
    }

    /**
     * Executes on LongClick(). Triggers displaying
     * contextual menu with a delete option.
     */
    fun displayMenuWithDelete(movie: Movie){
        _contextualMenuEvent.value = SingleEvent(movie)
    }

    /**
     * Executes once [Movie] item is deleted
     * from the list of favourites. Triggers
     * changes to the database via update().
     */
    fun removeFromFavourite(movie: Movie) {
        movie.isFavourite = false

        update(movie)
    }

    /**
     * Executes once a [Movie] item is deleted.
     * Triggers displaying SnackBar with an
     * option to undo delete.
     */
    fun displaySnackBarWithUndo(movie: Movie) {
        _snackBarEvent.value = SingleEvent(movie)
    }

    /**
     * Executes once undo in the SnackBar is selected.
     * Ensures the item backs on the same position
     * since no changes in save_date.
     */
    fun undoRemove(movie: Movie) {
        movie.isFavourite = true
        update(movie)
    }

    /**
     * Helper method to update the changes
     * to the Movie via repository.
     */
    private fun update(movie: Movie) {
        viewModelScope.launch {
            repository.update(movie)
        }
    }
}