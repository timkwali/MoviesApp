package com.example.common.di

import android.app.Application
import androidx.room.Room
import com.example.common.data.db.MoviesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMoviesDatabase(app: Application): MoviesDatabase {
        return Room.databaseBuilder(
            app,
            MoviesDatabase::class.java,
            MoviesDatabase.DATABASE_NAME
        ).build()
    }
}