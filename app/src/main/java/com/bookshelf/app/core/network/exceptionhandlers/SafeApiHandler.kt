package com.bookshelf.app.core.network.exceptionhandlers

import com.bookshelf.app.core.network.NetworkResponse
import retrofit2.Response

suspend inline fun <reified T> safeApiCall(crossinline function: suspend () -> Response<T>): NetworkResponse<T> {
    return try {
        // blocking block
        function.invoke().run {
            if (isSuccessful && body() != null) {
                NetworkResponse.Success(body()!!)
            } else {
                ErrorHandler.parseError(errorBody(), code())
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ErrorHandler.handleException(e)
    }
}