package com.example.movieslist.domain.usecase

import androidx.paging.PagingData
import com.example.common.data.db.model.Movie
import com.example.common.data.db.model.MovieItemMapper
import com.example.common.data.networkBoundResource
import com.example.common.domain.repository.MoviesRepository
import com.example.common.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovies @Inject constructor(
    private val repository: MoviesRepository
) {

    suspend operator fun invoke(): Flow<Resource<List<Movie>>>  = networkBoundResource(
        query = {
            repository.getDbMovies()
        },
        fetch = {
            repository.getMovies()
        },
        saveFetchResult = { popularMoviesFlow ->
             val movie = popularMoviesFlow.results.map {
                 MovieItemMapper().mapToDomain(it)
             }
             repository.saveMovies(movie)
        }
    )
}