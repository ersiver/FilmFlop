package com.ersiver.filmflop.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ersiver.filmflop.R
import com.ersiver.filmflop.databinding.FragmentDetailBinding
import com.ersiver.filmflop.model.Movie
import com.ersiver.filmflop.util.EventObserver
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private val binding: FragmentDetailBinding by lazy {
        FragmentDetailBinding.inflate(layoutInflater)
    }

    val detailViewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    private val movie: Movie by lazy {
        args.movie
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = detailViewModel
            detailToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Pass the selected movie id.
        detailViewModel.getMovie(movie.id)

        detailViewModel.showTrailerEvent.observe(viewLifecycleOwner, EventObserver {
            showMovieTrailer(it)
        })
    }

    /**
     * Invoked when the changes on the
     * [DetailViewModel.showTrailerEvent] observed.
     *
     * Opens the movie trial on YouTube.
     */
    private fun showMovieTrailer(url: String) {
        try {
            val uri = Uri.parse(url)
            val packageManager = requireNotNull(context).packageManager
            // Try to generate a direct intent to the YouTube app
            var intent = Intent(Intent.ACTION_VIEW, uri)
            if (intent.resolveActivity(packageManager) == null) {
                // YouTube app isn't found, use the web url
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), resources.getString(R.string.trailerError), Toast.LENGTH_SHORT).show()
            Timber.i("Failed to display trailer. ${e.message}")
        }
    }
}