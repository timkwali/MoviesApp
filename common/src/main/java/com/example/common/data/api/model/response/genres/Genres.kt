package com.example.common.data.api.model.response.genres


import com.google.gson.annotations.SerializedName

data class Genres(
    @SerializedName("genres")
    val genres: List<Genre>?
)