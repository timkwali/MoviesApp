package com.example.moviedetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.data.api.model.response.genres.Genre
import com.example.common.data.db.model.Movie
import com.example.common.utils.Resource
import com.example.common.utils.Utils.getGenreNameFromId
import com.example.moviedetails.domain.usecase.GetGenre
import com.example.moviedetails.domain.usecase.GetMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovie: GetMovie,
    private val getGenre: GetGenre
): ViewModel() {

     var movie: MutableStateFlow<Resource<Movie>?> = MutableStateFlow(null)
        private set

    var genres: MutableStateFlow<Resource<List<Genre>>?> = MutableStateFlow(null)
        private set

    var genreNames: MutableStateFlow<Resource<List<String>>?> = MutableStateFlow(null)
        private set

    init {
        getGenres()
    }


    fun getMovieById(movieId: Int) = viewModelScope.launch {
        movie.value = Resource.Loading()
        getMovie(movieId).collect {
            movie.value = it
        }
    }

    fun getGenres() = viewModelScope.launch {
        genres.value = Resource.Loading()
        getGenre().collect {
            genres.value = it
        }
    }

    fun getGenreNames(genreIds: List<Int>, genres: List<Genre>) {
        genreNames.value = Resource.Success(getGenreNameFromId(genreIds, genres))
    }
}