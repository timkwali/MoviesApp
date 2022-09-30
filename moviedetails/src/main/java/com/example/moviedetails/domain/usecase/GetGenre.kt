package com.example.moviedetails.domain.usecase

import com.example.common.data.api.model.response.genres.Genre
import com.example.common.data.db.model.Movie
import com.example.common.domain.repository.MoviesRepository
import com.example.common.utils.ApiError
import com.example.common.utils.Resource
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGenre @Inject constructor (
    private val repository: MoviesRepository,
    private val apiError: ApiError
) {
    suspend operator fun invoke() = flow<Resource<List<Genre>>> {

        try {
            repository.getDbGenres().collect {
                if(it == null) {
                    emit(Resource.Error("Genres could not be found"))
                } else {
                    emit(Resource.Success(it))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(apiError.extractErrorMessage(e)))
        }
    }
}