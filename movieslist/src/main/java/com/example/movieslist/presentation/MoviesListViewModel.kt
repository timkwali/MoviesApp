package com.example.movieslist.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.domain.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val repository: MoviesRepository
): ViewModel()  {

    fun getMovies() = viewModelScope.launch {
        repository.getPopularMovies().collect {
            Log.d("moviesREsponse", "movies----> $it")
        }
    }
}