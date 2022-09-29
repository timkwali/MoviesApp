package com.example.common.data.repository

import com.example.common.data.api.MoviesApi
import com.example.common.domain.repository.MoviesRepository
import com.example.common.data.api.model.response.popularmovies.PopularMoviesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi
): MoviesRepository {
    override suspend fun getPopularMovies(): Flow<PopularMoviesResponse> {
        return flowOf(moviesApi.getPopularMovies())
    }
}