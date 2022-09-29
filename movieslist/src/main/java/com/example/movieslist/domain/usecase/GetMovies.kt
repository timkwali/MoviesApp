package com.example.movieslist.domain.usecase

import androidx.paging.PagingData
import com.example.common.data.db.model.Movie
import com.example.common.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovies @Inject constructor(
    private val repository: MoviesRepository
) {

    operator fun invoke(): Flow<PagingData<Movie>> {
        return repository.getMovies()
    }
}