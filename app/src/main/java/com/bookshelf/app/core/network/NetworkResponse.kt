package com.bookshelf.app.core.network

import com.bookshelf.app.core.network.exceptionhandlers.ApiFailureException

sealed class NetworkResponse<out T> {
    class Success<out T>(val data: T) : NetworkResponse<T>()
    class Error<out T>(val error: ApiFailureException? = null) : NetworkResponse<T>()
    class Loading<out T>(val isLoading: Boolean) : NetworkResponse<T>()
}