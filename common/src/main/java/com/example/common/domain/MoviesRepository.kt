package com.example.common.domain

import com.timkwali.tmdmovies.common.data.model.popularmovies.PopularMoviesResponse
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getPopularMovies(): Flow<PopularMoviesResponse>
}