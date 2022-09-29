package com.example.common.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.common.utils.Constants

@Entity(tableName = Constants.MOVIES_TABLE)
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val releaseDate: String,
    val overview: String,
    val voteAverage: Double,
    val genreIds: List<Int>,
    val voteCount: Int,
    var posterPath: String
)
