package com.ersiver.filmflop.ui.search

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ersiver.filmflop.R
import com.ersiver.filmflop.databinding.FragmentSearchBinding
import com.ersiver.filmflop.model.Movie
import com.ersiver.filmflop.ui.common.MovieAdapter
import com.ersiver.filmflop.util.DEFAULT_COLUMN_COUNT
import com.ersiver.filmflop.util.EventObserver
import com.ersiver.filmflop.util.SEARCH_GRID_COLUMN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    val searchViewModel: SearchViewModel by viewModels()
    private lateinit var adapter: MovieAdapter
    private val binding: FragmentSearchBinding by lazy {
        FragmentSearchBinding.inflate(layoutInflater)
    }
    private val sharedPrefs: SharedPreferences by lazy {
        requireActivity().getSharedPreferences(
            resources.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = initAdapter()
        binding.apply {
            viewModel = searchViewModel
            list.adapter = adapter
            lifecycleOwner = viewLifecycleOwner
            searchToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getSavedDataAndStartViewModel()

        searchViewModel.navigateToDetailEvent.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToDetailFragment(
                    it
                )
            )
        })

        searchViewModel.columnCount.observe(viewLifecycleOwner, Observer { columnCount ->
            saveColumnCountToSharedPrefs(columnCount)
        })
    }

    /**
     * Init adapter with onClick listener.
     */
    private fun initAdapter(): MovieAdapter {
        return MovieAdapter(object :
            MovieAdapter.MovieAdapterListener {

            override fun onClick(view: View, movie: Movie) {
                searchViewModel.navigateToDetail(movie)
            }

            override fun onLongClick(movie: Movie): Boolean {
                return false
            }
        })
    }

    /**
     * Returns last saved column count. Ensures that's
     * properly initialized with the default value
     * when this method is called for the first time.
     *
     * Updates ViewModel so it triggers setting of
     * recyclerview's span count via BindingAdapter.
     */
    private fun getSavedDataAndStartViewModel() {
        val columnCountGrid = sharedPrefs.getInt(SEARCH_GRID_COLUMN, DEFAULT_COLUMN_COUNT)
        searchViewModel.start(columnCountGrid)
    }

    /**
     * Save a new column count to the shared prefs.
     */
    private fun saveColumnCountToSharedPrefs(columnCount: Int) {
        with(sharedPrefs.edit()){
            putInt(SEARCH_GRID_COLUMN, columnCount)
            apply()
        }
    }
}