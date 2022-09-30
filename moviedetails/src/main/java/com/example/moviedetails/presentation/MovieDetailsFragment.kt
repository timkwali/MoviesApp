package com.example.moviedetails.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.common.data.db.model.Movie
import com.example.common.utils.Constants.IMAGE_URL
import com.example.common.utils.Resource
import com.example.common.utils.Utils.loadImage
import com.example.moviedetails.R
import com.example.moviedetails.databinding.FragmentMovieDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: MovieDetailsFragmentArgs by navArgs()
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        getMovieDetails()
        observeMovieDetails()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setListeners() {
        binding.apply {
            backIb.setOnClickListener { findNavController().popBackStack() }
            retryBtn.setOnClickListener { getMovieDetails() }
        }
    }

    private fun setUpViews(movie: Movie) {
        binding.apply {
            val imageUrl = IMAGE_URL + movie.posterPath
            image.loadImage(imageUrl)
            movieName.text = movie.title
            overview.text = movie.overview
            voteAverage.text = movie.voteAverage.toString()
            voteCount.text = "${movie.voteCount} Votes"
            releaseDate.text = movie.releaseDate
            genre.text = movie.genreIds.toString()
        }
    }

    private fun getMovieDetails() {
        args.movieId.let {
            movieDetailsViewModel.getMovieById(it.toInt())
        }
    }

    private fun observeMovieDetails() {
        lifecycleScope.launchWhenStarted {
            movieDetailsViewModel.movie.collect {
                binding.apply {
                    loading.isVisible = it is Resource.Loading
                    errorTv.isVisible = it is Resource.Error
                    errorTv.text = it?.message
                    if(it is Resource.Success)  setUpViews(it.data!!)
                }
            }
        }
    }
}