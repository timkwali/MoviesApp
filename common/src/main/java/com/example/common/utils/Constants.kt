package com.example.common.utils

import com.example.common.BuildConfig

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val LANGUAGE = "en-US"
    const val API_KEY = BuildConfig.API_KEY
    const val PAGE_NUMBER = 1
    const val PER_PAGE = 9

    const val MOVIES_DATABASE = "movies_database"
    const val MOVIES_TABLE = "movies_table"
    const val REMOTE_KEYS_TABLE = "remote_keys"
}