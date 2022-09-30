package com.example.common.data.api.model.response.genres


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.common.utils.Constants.MOVIES_GENRE_TABLE
import com.google.gson.annotations.SerializedName

@Entity(tableName = MOVIES_GENRE_TABLE)
data class Genre(
    @PrimaryKey
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)