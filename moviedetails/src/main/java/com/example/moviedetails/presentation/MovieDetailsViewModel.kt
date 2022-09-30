package com.example.moviedetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.data.db.model.Movie
import com.example.common.utils.Resource
import com.example.moviedetails.domain.usecase.GetMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovie: GetMovie
): ViewModel() {
     var movie: MutableStateFlow<Resource<Movie>?> = MutableStateFlow(null)
        private set


    fun getMovieById(movieId: Int) = viewModelScope.launch {
        movie.value = Resource.Loading()
        getMovie(movieId).collect {
            movie.value = it
        }
    }
}