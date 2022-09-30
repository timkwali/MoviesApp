package com.example.movieslist.utils

import com.example.common.data.api.model.response.genres.Genre
import com.example.common.data.api.model.response.genres.Genres
import com.example.common.data.api.model.response.popularmovies.PopularMovie
import com.example.common.data.api.model.response.popularmovies.PopularMoviesResponse
import com.example.common.data.db.model.Movie
import com.example.common.data.db.model.MovieItemMapper

object Constants {
    private val popularMovie = PopularMovie(
        1, false, "/5GA3vV1aWWHTSDO5eno8V5zDo8r.jpg", listOf(1, 2), "en-US",
        "Orphan: First Kill", "After escaping from an Estonian psychiatric facility, Leena Klammer travels to America by impersonating Esther, the missing daughter of a wealthy family. But when her mask starts to slip, she is put against a mother who will protect her family from the murderous “child” at any cost.",
        6491.393, "/wSqAXL1EHVJ3MOnJzMhUngc8gFs.jpg", "2022-07-27", "Orphan: First Kill",
        false, 7.0, 758
    )

    val popularMovies = listOf<PopularMovie>(
        popularMovie.copy(id = 1), popularMovie.copy(id = 2), popularMovie.copy(id = 3), popularMovie.copy(id = 4)
    )
    val popularMoviesResponse = PopularMoviesResponse(
        1, popularMovies, 3, 9
    )

    suspend fun List<PopularMovie>.toMovieList(): List<Movie> {
        return this.map {
            MovieItemMapper().mapToDomain(it)
        }
    }

    val genreList = listOf(
        Genre(1, "Action"), Genre(2, "Comedy"),
        Genre(3, "Thriller"), Genre(4, "Romance")
    )
    val genreResponse = Genres(genreList)
}