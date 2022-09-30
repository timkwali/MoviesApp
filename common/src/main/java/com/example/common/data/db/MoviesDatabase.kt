package com.example.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.common.data.api.model.response.genres.Genre
import com.example.common.data.db.dao.GenreDao
import com.example.common.data.db.dao.MoviesDao
import com.example.common.data.db.model.Movie
import com.example.common.utils.Constants.MOVIES_DATABASE

@Database(
    entities = [Movie::class, Genre::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
    abstract fun genreDao(): GenreDao

    companion object {
        const val DATABASE_NAME = MOVIES_DATABASE
    }
}