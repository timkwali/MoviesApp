package com.example.common.domain.repository

import com.example.common.data.api.model.response.popularmovies.PopularMoviesResponse
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getPopularMovies(): Flow<PopularMoviesResponse>
}