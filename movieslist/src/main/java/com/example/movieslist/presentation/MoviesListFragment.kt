package com.example.movieslist.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.data.db.model.Movie
import com.example.common.utils.Resource
import com.example.common.utils.Utils.showSnackBar
import com.example.movieslist.databinding.FragmentMoviesListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@AndroidEntryPoint
class MoviesListFragment : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val binding: FragmentMoviesListBinding get() = _binding!!
    private val moviesViewModel: MoviesListViewModel by viewModels()
    private lateinit var moviesListAdapter: MoviesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesListAdapter = MoviesListAdapter {
            onMovieClick(it)
        }
        setListeners()
        observeMoviesListResponse()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setListeners() {
        binding.apply {
            swipeSrl.setOnRefreshListener {
                moviesViewModel.getMoviesList()
            }
            retryBtn.setOnClickListener {
                moviesViewModel.getMoviesList()
                moviesViewModel.getGenreList()
            }
        }
    }

    private fun observeMoviesListResponse() {
        lifecycleScope.launchWhenStarted {
            moviesViewModel.moviesListResponse.collect {
                binding.apply {
                    Log.d("ldskfja", "ldfak--->${it?.data}")
                    swipeSrl.isRefreshing = it is Resource.Loading
                    emptyListTv.isVisible = it is Resource.Error && (it.data == null || it.data?.isEmpty() == true)
                    retryBtn.isVisible = it is Resource.Error && (it.data == null || it.data?.isEmpty() == true)
                    emptyListTv.text = it?.message
                    if((it?.data != null || it?.data?.isEmpty() == true) && it is Resource.Error) showSnackBar(it.message)
                    if(it?.data != null) {
                        setUpRecyclerView(it.data!!)
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView(movies: List<Movie>) {
        binding.moviesListRv.adapter = moviesListAdapter
        binding.moviesListRv.layoutManager = LinearLayoutManager(requireContext())
        moviesListAdapter.submitList(movies)
    }

    private fun onMovieClick(movie: Movie) {
        val request = NavDeepLinkRequest.Builder
            .fromUri("android-app://com.example.moviedetails.presentation.MovieDetailsFragment/${movie.id}".toUri())
            .build()
        findNavController().navigate(request)
    }

}