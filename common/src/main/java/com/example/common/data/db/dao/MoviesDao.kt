package com.example.common.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.common.data.db.model.Movie

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    @Query("SELECT * FROM movies_table")
    fun getAllMovies(): PagingSource<Int, Movie>

    @Query("SELECT * FROM movies_table WHERE id LIKE :id")
    fun getMovieById(id: String): PagingSource<Int, Movie>

    @Query("DELETE FROM movies_table")
    suspend fun clearMovies()

}