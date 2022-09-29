package com.example.common.data.api

import com.example.common.utils.Constants
import com.example.common.data.api.model.response.popularmovies.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = Constants.PAGE_NUMBER,
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("language") language: String = Constants.LANGUAGE
    ): PopularMoviesResponse
}