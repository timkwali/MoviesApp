package com.example.common.utils

import java.io.IOException
import java.util.concurrent.TimeoutException

open class NetworkException(val code: Int, message: String) : IOException(message)
class NoNetworkException : IOException("No internet connection available")
class ServerException(code: Int, message: String) : NetworkException(code, message)
class RequestTimeoutException: TimeoutException("The request timed out")