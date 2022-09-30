package com.example.common.utils

import android.content.Context
import com.example.common.R
import com.google.gson.Gson
import retrofit2.HttpException
import javax.inject.Inject

class ApiError @Inject constructor(private val gson: Gson, private val context: Context) {


    fun extractErrorMessage(throwable: Throwable?): String {

        return when (throwable) {
            is HttpException -> {
                val response = throwable.response()
                val json = response?.errorBody()?.string()

                json?.let {
                    val error = gson.fromJson(json, ErrorResponse::class.java)
                    return@let if (error.message.isNotEmpty()) error.message
                    else context.getString(R.string.unknown_error)
                } ?: context.getString(R.string.unknown_error)
            }
            is NoNetworkException -> context.getString(R.string.check_connection)
            is ServerException -> context.getString(R.string.unable_to_connect_to_server)
            is RequestTimeoutException -> context.getString(R.string.the_request_timed_out)
            else -> throwable?.localizedMessage ?: context.getString(R.string.unknown_error)
        }
    }


    data class ErrorResponse(
        val success: String,
        val message: String
    )
}