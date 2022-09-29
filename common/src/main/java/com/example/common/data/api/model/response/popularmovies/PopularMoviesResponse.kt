package com.example.common.data.api.model.response.popularmovies

import com.example.common.data.api.model.response.popularmovies.PopularMovie
import com.google.gson.annotations.SerializedName

data class PopularMoviesResponse(
    val page: Int?,
    val results: List<PopularMovie>,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)

