package com.ersiver.filmflop.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ersiver.filmflop.databinding.ItemMovieBinding
import com.ersiver.filmflop.model.Movie


/**
 * View Holder for a [Movie] RecyclerView list item.
 */
class MovieViewHolder(
    private val binding: ItemMovieBinding,
    val listener: MovieAdapter.MovieAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.listener = listener
    }

    fun bind(item: Movie?) {
        binding.run {
            movie = item
            executePendingBindings()
        }
    }

    companion object {
        fun from(parent: ViewGroup, listener: MovieAdapter.MovieAdapterListener): MovieViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemMovieBinding.inflate(layoutInflater, parent, false)
            return MovieViewHolder(
                binding,
                listener
            )
        }
    }
}