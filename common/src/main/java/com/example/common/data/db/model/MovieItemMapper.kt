package com.example.common.data.db.model

import com.example.common.data.api.model.response.popularmovies.PopularMovie
import com.example.common.utils.DomainMapper

class MovieItemMapper: DomainMapper<PopularMovie, Movie> {
    override suspend fun mapToDomain(entity: PopularMovie): Movie {
        return Movie(
            id = entity.id ?: 0,
            title = entity.title ?: "",
            releaseDate = entity.releaseDate ?: "",
            overview = entity.overview ?: "",
            voteAverage = entity.voteAverage ?: 0.0,
            genreIds = entity.genreIds ?: emptyList(),
            voteCount = entity.voteCount ?: 0,
            posterPath = entity.posterPath ?: ""
        )
    }
}