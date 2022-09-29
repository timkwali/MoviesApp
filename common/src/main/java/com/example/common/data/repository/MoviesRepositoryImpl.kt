package com.example.common.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.common.data.api.MoviesApi
import com.example.common.domain.repository.MoviesRepository
import com.example.common.data.api.model.response.popularmovies.PopularMoviesResponse
import com.example.common.data.db.MoviesDatabase
import com.example.common.data.db.model.Movie
import com.example.common.domain.pagination.MoviesRemoteMediator
import com.example.common.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi,
    private val moviesDatabase: MoviesDatabase
): MoviesRepository {
    override suspend fun getMovies(): Flow<PagingData<Movie>> {
        // appending '%' so we can allow other characters to be before and after the query string
//        val dbQuery = "%${login.replace(' ', '%')}%"
        val pagingSourceFactory =  { moviesDatabase.moviesDao().getAllMovies()}

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PER_PAGE,
                prefetchDistance = 4,
                enablePlaceholders = false
            ),
            remoteMediator = MoviesRemoteMediator(
                moviesApi,
                moviesDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}