package com.ersiver.filmflop.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ersiver.filmflop.R
import com.ersiver.filmflop.databinding.FragmentHomeBinding
import com.ersiver.filmflop.model.Movie
import com.ersiver.filmflop.ui.common.MovieAdapter
import com.ersiver.filmflop.util.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class HomeFragment : Fragment() {
    val homeViewModel by viewModels<HomeViewModel>()

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private val sharedPrefs: SharedPreferences by lazy {
        requireActivity().getSharedPreferences(
            resources.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
    }

    private lateinit var homeAdapter: MovieAdapter
    private var actionMode: ActionMode? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeAdapter = initAdapter()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = homeViewModel
            list.adapter = homeAdapter
            setupToolbar()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getSavedDataAndStartViewModel()

        homeViewModel.navigateToDetail.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToDetailFragment(it))
        })

        homeViewModel.navigateToSearch.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToSearchFragment())
        })

        homeViewModel.sortType.observe(viewLifecycleOwner, Observer { sort ->
            saveSortToSharedPrefs(sort)
            homeViewModel.getFavourites(sort)
        })

        homeViewModel.undoRemoveEvent.observe(viewLifecycleOwner, EventObserver {
            displaySnackBarWithUndoRemove(it)
        })

        homeViewModel.columnCount.observe(viewLifecycleOwner, Observer { columnCount ->
            saveColumnCountToSharedPrefs(columnCount)
        })
    }

    /**
     * Setup custom toolbar with NavigationIcon, which opens a drawer.
     */
    private fun FragmentHomeBinding.setupToolbar() {
        homeToolbar.setNavigationOnClickListener {
            requireActivity().drawer_layout.openDrawer(
                GravityCompat.START
            )
        }
    }

    /**
     * Returns last set sort type and column count.
     * Ensures that's properly initialized with the default value
     * when this method is called for the first time.
     */
    private fun getSavedDataAndStartViewModel() {
        val currentSort = sharedPrefs.getString(PREFS_SORT, DEFAULT_SORT).toString()
        val columnCountGrid = sharedPrefs.getInt(HOME_GRID_COLUMN, DEFAULT_COLUMN_COUNT)

        homeViewModel.start(currentSort, columnCountGrid)
    }

    /**
     * Display snackBar when the user remove the movie
     * from the list with an option to undo delete.
     */
    private fun displaySnackBarWithUndoRemove(movie: Movie) {
        Snackbar.make(
            binding.list,
            "${movie.title} " + resources.getString(R.string.undo_delete),
            Snackbar.LENGTH_SHORT
        ).setAction(resources.getString(R.string.undo_action)) {
            homeViewModel.undoRemove(movie)
        }.show()
    }

    /**
     * Save a new sort type to the shared prefs.
     */
    private fun saveSortToSharedPrefs(sort: String?) {
        with(sharedPrefs.edit()){
            putString(PREFS_SORT, sort)
            apply()
        }
    }

    /**
     * Save a new column count to the shared prefs.
     */
    private fun saveColumnCountToSharedPrefs(columnCount: Int) {
        with(sharedPrefs.edit()){
            putInt(HOME_GRID_COLUMN, columnCount)
            apply()
        }
    }

    /**
     * Init adapter with onClick and onLongClick listeners.
     */
    private fun initAdapter(): MovieAdapter {
        return MovieAdapter(object :
            MovieAdapter.MovieAdapterListener {

            override fun onClick(view: View, movie: Movie) {
                homeViewModel.navigateToDetail(movie)
            }

            override fun onLongClick(movie: Movie): Boolean {
                return when (actionMode) {
                    null -> {
                        actionMode = requireActivity().startActionMode(object :
                            ActionMode.Callback {

                            // Called when the user selects a contextual menu item
                            override fun onActionItemClicked(
                                mode: ActionMode, item: MenuItem
                            ): Boolean {
                                return when (item.itemId) {
                                    R.id.delete -> {
                                        homeViewModel.removeFromFavourite(movie)
                                        homeViewModel.undoRemoveSnackBar(movie)
                                        mode.finish() // Action picked, so close the CAB
                                        true
                                    }
                                    else -> false
                                }
                            }

                            // Called when the action mode is created; startActionMode() was called
                            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                                val inflater: MenuInflater = mode.menuInflater
                                inflater.inflate(R.menu.home_contextual_menu, menu)
                                return true
                            }

                            // Return false if nothing is done
                            override fun onPrepareActionMode(
                                mode: ActionMode?,
                                menu: Menu?
                            ): Boolean {
                                return false
                            }

                            // Called when the user exits the action mode
                            override fun onDestroyActionMode(mode: ActionMode?) {
                                actionMode = null
                            }
                        })
                        actionMode?.title = resources.getString(R.string.cab_title)
                        true
                    }
                    else -> false
                }
            }
        })
    }
}