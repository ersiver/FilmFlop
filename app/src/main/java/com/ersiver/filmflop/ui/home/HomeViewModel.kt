package com.ersiver.filmflop.ui.home

import android.view.MenuItem
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
    val favouriteMovies: LiveData<List<Movie>>
        get() = _favouriteMovies

    private val _navigateToDetail = MutableLiveData<SingleEvent<Movie>>()
    val navigateToDetail: LiveData<SingleEvent<Movie>>
        get() = _navigateToDetail

    private val _navigateToSearch = MutableLiveData<SingleEvent<Boolean>>()
    val navigateToSearch: LiveData<SingleEvent<Boolean>>
        get() = _navigateToSearch

    private val _undoRemoveEvent = MutableLiveData<SingleEvent<Movie>>()
    val undoRemoveEvent: LiveData<SingleEvent<Movie>>
        get() = _undoRemoveEvent

    private val _sortType = MutableLiveData<String>()
    val sortType: LiveData<String>
        get() = _sortType

    private val _columnCount = MutableLiveData<Int>()
    val columnCount: LiveData<Int>
        get() = _columnCount

    /**
     * Called in HomeFragment onActivityCreated.
     */
    fun start(currentSort: String, columnCountGrid: Int) {
        _sortType.value = currentSort
        _columnCount.value = columnCountGrid
    }

    /**
     * Executes whenever [MenuItem] action_sort is clicked.
     */
    fun updateSortType() {
        val newSortType = if (_sortType.value == DEFAULT_SORT) TITLE_SORT else DEFAULT_SORT
        _sortType.value = newSortType
    }

    /**
     * Executes once the [MenuItem] action_layout is clicked.
     * Gets the appropriate column count and sets GridLayoutManager
     * span count via BindingAdapter.
     */
    fun updateGridLayout() {
        val newColumnCount = if(_columnCount.value ==1)2 else 1
        _columnCount.value = newColumnCount
    }

    /**
     * Called when app launches and whenever sort type changes.
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
     * Called once [MenuItem] nav_search is clicked.
     * Triggers navigation to the SearchFragment
     */
    fun navigateToNavSearch() {
        _navigateToSearch.value = SingleEvent(true)
    }

    /**
     * Executes once a [Movie] is selected.
     * Triggers navigation to DetailFragment.
     */
    fun navigateToDetail(movie: Movie) {
        _navigateToDetail.value = SingleEvent(movie)
    }

    /**
     * Executes once [Movie] item is deleted from the list of favourite.
     */
    fun setFavourite(movie: Movie) {
        val lastFavouriteStatus = movie.isFavourite
        movie.isFavourite = !lastFavouriteStatus
        movie.saveDate = System.currentTimeMillis()

        viewModelScope.launch {
            repository.update(movie)
        }
    }

    /**
     * Executes once a [Movie] item is deleted
     * giving option to undo delete.
     */
    fun undoRemove(movie: Movie) {
        _undoRemoveEvent.value = SingleEvent(movie)
    }
}