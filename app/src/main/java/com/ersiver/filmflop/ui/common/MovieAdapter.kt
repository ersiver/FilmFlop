package com.ersiver.filmflop.ui.common

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ersiver.filmflop.model.Movie

/**
 * Adapter for the list of movies.
 */
class MovieAdapter(private val listener: MovieAdapterListener) :
    ListAdapter<Movie, MovieViewHolder>(
        DIFF_CALLBACK
    ) {

    interface MovieAdapterListener {
        fun onClick(view: View, movie: Movie)
        fun onLongClick(movie: Movie): Boolean
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.from(
            parent,
            listener
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)

        holder.itemView.setOnClickListener {
            listener.onClick(holder.itemView, movie)
        }

        holder.itemView.setOnLongClickListener {
            listener.onLongClick(movie)
        }

        holder.bind(movie)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Movie> = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem== newItem
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id== newItem.id
            }
        }
    }
}
