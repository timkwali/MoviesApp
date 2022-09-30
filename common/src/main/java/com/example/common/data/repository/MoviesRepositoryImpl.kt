package com.example.common.data.repository

import androidx.room.withTransaction
import com.example.common.data.api.MoviesApi
import com.example.common.data.api.model.response.genres.Genre
import com.example.common.data.api.model.response.genres.Genres
import com.example.common.data.api.model.response.popularmovies.PopularMoviesResponse
import com.example.common.domain.repository.MoviesRepository
import com.example.common.data.db.MoviesDatabase
import com.example.common.data.db.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi,
    private val moviesDatabase: MoviesDatabase
): MoviesRepository {
    private val moviesDao = moviesDatabase.moviesDao()
    private val genreDao = moviesDatabase.genreDao()

    override suspend fun getMovies(): PopularMoviesResponse {
        return moviesApi.getPopularMovies()
    }

    override fun getDbMovies(): Flow<List<Movie>> {
        return moviesDao.getAllMovies()
    }

    override suspend fun saveMovies(movies: List<Movie>) {
        moviesDatabase.withTransaction {
            deleteAllMovies()
            moviesDao.insertAll(movies)
        }
    }

    override fun getMovieById(id: Int): Flow<Movie> {
        return moviesDao.getMovieById(id)
    }

    override suspend fun deleteAllMovies() {
        moviesDao.clearMovies()
    }

    override suspend fun getGenres(): Genres {
        return moviesApi.getMoviesGenre()
    }

    override fun getDbGenres(): Flow<List<Genre>> {
        return moviesDatabase.genreDao().getMoviesGenres()
    }

    override suspend fun saveGenres(genres: List<Genre>) {
        moviesDatabase.withTransaction {
            deleteAllGenres()
            genreDao.saveMoviesGenres(genres)
        }
    }

    override fun getGenreById(id: Int): Flow<Genre> {
        return genreDao.getGenreById(id)
    }

    override suspend fun deleteAllGenres() {
        genreDao.deleteAllMoviesGenres()
    }
}