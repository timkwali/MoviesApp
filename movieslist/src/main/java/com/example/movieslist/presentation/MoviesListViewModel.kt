package com.example.movieslist.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.data.api.model.response.genres.Genre
import com.example.common.data.db.model.Movie
import com.example.common.utils.Resource
import com.example.movieslist.domain.usecase.GetGenres
import com.example.movieslist.domain.usecase.GetMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val getMovies: GetMovies,
    private val getGenres: GetGenres
): ViewModel()  {

    var moviesListResponse: MutableStateFlow<Resource<List<Movie>>?> = MutableStateFlow(null)
        private set

    var genreList: MutableStateFlow<Resource<List<Genre>>?> = MutableStateFlow(null)
        private set

    init {
        getMoviesList()
        getGenreList()
    }

    fun getMoviesList() = viewModelScope.launch {
        moviesListResponse.value = Resource.Loading()
        getMovies.invoke().collect {
            moviesListResponse.value = it
        }
    }

    fun getGenreList() = viewModelScope.launch {
        genreList.value = Resource.Loading()
        getGenres().collect {
            genreList.value = it
        }
    }
}