package com.example.movieslist.domain.usecase

import com.example.common.data.api.model.response.genres.Genre
import com.example.common.data.networkBoundResource
import com.example.common.domain.repository.MoviesRepository
import com.example.common.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGenres @Inject constructor(
    private val repository: MoviesRepository
) {

    suspend operator fun invoke(): Flow<Resource<List<Genre>>>  = networkBoundResource(
        query = {
            repository.getDbGenres()
        },
        fetch = {
            repository.getGenres()
        },
        saveFetchResult = { genres ->
            genres.genres?.let { repository.saveGenres(it) }
        }
    )
}