package com.ersiver.filmflop.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ersiver.filmflop.R
import com.ersiver.filmflop.model.Movie
import com.ersiver.filmflop.ui.common.MovieAdapter

/**
 * Binding adapter used to to display movie poster with Glide.
 */
@BindingAdapter("imageUrl")
fun ImageView.bindImage(posterPath: String?) {
    if(!posterPath.isNullOrEmpty())
    Glide.with(context)
        .load(posterPath.concatUrl())
        .apply(
            RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_logo)
        )
        .into(this)
    else setImageResource(R.drawable.ic_launcher)
}

/**
 * Binding adapter used to to display year of release.
 */
@BindingAdapter("dateFormatted")
fun TextView.bindReleaseDate(releaseDate: String?) {
    text = releaseDate?.surroundedWithParenthesis()
}

/**
 * Binding adapter used to to display movie's genre(s).
 */
@ExperimentalStdlibApi
@BindingAdapter("genres")
fun TextView.bindGenres(genres: List<String>?) {
    text = genres?.asStringWithComas()
}

/**
 * Binding adapter used to display a movie rating in the 0-10 scale.
 */
@BindingAdapter("rating")
fun TextView.bindRating(rating: Double?) {
    val toBeDisplayed = "$rating/10"
    text = toBeDisplayed
}

/**
 * Binding adapter used to load a correct icon to the [ImageButton] in the
 * detailToolbar enabling saving or removing a movie from the list of favourite.
 */
@BindingAdapter("actionIcon")
fun ImageButton.setIcon(isFavourite: Boolean) {
    val resId = if (isFavourite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
    setImageResource(resId)
}

/**
 * Binding adapter used to submit list of  searchedMovies to the [MovieAdapter].
 */
@BindingAdapter("movieListData")
fun RecyclerView.bindRecyclerView(result: Resource<List<Movie>>?) {
    val adapter = adapter as MovieAdapter
    adapter.submitList(result?.data)
}

/**
 * Binding adapter used to submit list of favourite movies to the [MovieAdapter].
 */
@BindingAdapter("favouriteListData")
fun RecyclerView.bindFavouriteList(list: List<Movie>?) {
    val adapter = adapter as MovieAdapter
    adapter.submitList(list)
}

/**
 * Binding adapter used to to display an error
 * message on an attempt to submit an empty query.
 */
@BindingAdapter("errorMessage")
fun EditText.setError(isNullOrEmpty: Boolean) {
    if (isNullOrEmpty) error = resources.getString(R.string.required_text_query_input)
}

/**
 * Binding adapter used to manage keyboard and focus.
 */
@BindingAdapter("showSoftInput")
fun View.showKeyboard(isTyping: Boolean) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (isTyping) {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        requestFocus()
    } else {
        imm.hideSoftInputFromWindow(this.windowToken, 0)
        clearFocus()
    }
}

/**
 * Binding adapter used to set the Layout Manager
 * with appropriate column count.
 */
@BindingAdapter("gridColumnCount")
fun RecyclerView.layoutManager(gridColumnCount: Int){
    (this.layoutManager as GridLayoutManager).spanCount = gridColumnCount
}

/**
 * Binding adapter used to manage visibility
 * of the loading state layout.
 */
@BindingAdapter("visibility")
fun showHide(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
}


/**
 * Binding adapter used to set an appropriate action_layout icon
 * in the HomeFragment menu.
 *
 * If the current grid column count is set to 1, then the MenuItem
 * action_layout's icon is displayed as ic_grid, giving the option
 * to change the layout to the grid. In the opposite case the icon
 * will be displayed as the ic_list.
 */
@BindingAdapter("layoutActionIcon")
fun ImageButton.bindImage(gridColumnCount: Int){
    val resId = if (gridColumnCount == 1) R.drawable.ic_grid else R.drawable.ic_list
    setImageResource(resId)
}