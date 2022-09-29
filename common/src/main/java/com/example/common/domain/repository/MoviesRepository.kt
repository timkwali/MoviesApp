package com.example.common.domain.repository

import androidx.paging.PagingData
import com.example.common.data.api.model.response.popularmovies.PopularMoviesResponse
import com.example.common.data.db.model.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getMovies(): Flow<PagingData<Movie>>
}