package com.example.movieslist.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.data.db.model.Movie
import com.example.common.utils.Resource
import com.example.movieslist.domain.usecase.GetMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val getMovies: GetMovies,
): ViewModel()  {

    var moviesListResponse: MutableStateFlow<Resource<List<Movie>>?> = MutableStateFlow(null)
        private set

    init {
        getMoviesList()
    }

    fun getMoviesList() = viewModelScope.launch {
        moviesListResponse.value = Resource.Loading()
        getMovies.invoke().collect {
            moviesListResponse.value = it
        }
    }
}