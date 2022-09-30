package com.example.moviedetails.domain.usecase

import com.example.common.data.db.model.Movie
import com.example.common.domain.repository.MoviesRepository
import com.example.common.utils.ApiError
import com.example.common.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMovie @Inject constructor(
    private val repository: MoviesRepository,
    private val apiError: ApiError
) {
    suspend operator fun invoke(movieId: Int) = flow<Resource<Movie>> {

        try {
            repository.getMovieById(movieId).collect {
                if(it == null) {
                    emit(Resource.Error("Movie could not be found"))
                } else {
                    emit(Resource.Success(it))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(apiError.extractErrorMessage(e)))
        }
    }
}