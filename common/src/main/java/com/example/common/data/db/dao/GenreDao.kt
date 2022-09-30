package com.example.common.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.common.data.api.model.response.genres.Genre
import com.example.common.data.db.model.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Query("SELECT * FROM movies_genre")
    fun getMoviesGenres(): Flow<List<Genre>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMoviesGenres(moviesGenres: List<Genre>)

    @Query("SELECT * FROM movies_genre WHERE id LIKE :id")
    fun getGenreById(id: Int): Flow<Genre>

    @Query("DELETE FROM movies_genre")
    suspend fun deleteAllMoviesGenres()
}