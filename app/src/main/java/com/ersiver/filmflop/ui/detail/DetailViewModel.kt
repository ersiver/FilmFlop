package com.ersiver.filmflop.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ersiver.filmflop.model.Movie
import com.ersiver.filmflop.repository.MovieRepository
import com.ersiver.filmflop.util.SingleEvent
import kotlinx.coroutines.launch

class DetailViewModel @ViewModelInject constructor(val repository: MovieRepository) : ViewModel() {
    val movie: LiveData<Movie> = repository.movie
    val isFavourite: LiveData<Boolean> = repository.isFavourite
    private val _showTrailerEvent = MutableLiveData<SingleEvent<String>>()
    val showTrailerEvent: LiveData<SingleEvent<String>>
    get() = _showTrailerEvent

    /**
     * Gets selected movie via repository.
     */
    fun getMovie(id: Int) {
        viewModelScope.launch {
            repository.getMovie(id)
        }
    }

    /**
     * Executes once favourite icon is clicked.
     */
    fun setFavorite() {
        movie.value?.let { selectedMovie ->
            val lastFavouriteStatus = selectedMovie.isFavourite
            selectedMovie.isFavourite = !lastFavouriteStatus
            selectedMovie.saveDate = System.currentTimeMillis()

            updateMovie(selectedMovie)
        }
    }

    /**
     * Updates a movie is_favourite field
     * via repository.
     */
    private fun updateMovie(movie: Movie) {
        viewModelScope.launch {
            repository.update(movie)
        }
    }

    /**
     * Executes once trailes button is clicked.
     */
    fun onTrailerClicked(){
        movie.value?.let {
            _showTrailerEvent.value = SingleEvent(it.trailerUrl)
        }
    }
}