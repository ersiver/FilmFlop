package com.ersiver.filmflop.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ersiver.filmflop.model.Movie
import com.ersiver.filmflop.repository.MovieRepository
import com.ersiver.filmflop.util.SingleEvent
import kotlinx.coroutines.launch

class DetailViewModel @ViewModelInject constructor(
    val repository: MovieRepository
) : ViewModel() {

    val movie: LiveData<Movie> = repository.movie

    val isFavourite: LiveData<Boolean> = repository.isFavourite

    private val _showTrailerEvent = MutableLiveData<SingleEvent<String>>()

    val showTrailerEvent: LiveData<SingleEvent<String>> = _showTrailerEvent

    /**
     * Init selected movie via repository.
     */
    fun getMovie(id: Int) {
        viewModelScope.launch {
            repository.getMovie(id)
        }
    }

    /**
     * Executes once add_remove_action ImageButton is clicked.
     *
     * Triggers changes of the [Movie.isFavourite]to the
     * database via update() and sets the appropriate icon of the
     * add_remove_action ImageButton via BindingAdapter.
     */
    fun setFavorite() {
        movie.value?.let { selectedMovie ->
            val lastFavouriteStatus = selectedMovie.isFavourite
            selectedMovie.isFavourite = !lastFavouriteStatus
            selectedMovie.saveDate = System.currentTimeMillis()

            update(selectedMovie)
        }
    }

    /**
     * Executes once trailer Button is clicked.
     * Triggers navigation to the movie trailer.
     */
    fun onTrailerClicked() {
        movie.value?.let {
            _showTrailerEvent.value = SingleEvent(it.trailerUrl)
        }
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