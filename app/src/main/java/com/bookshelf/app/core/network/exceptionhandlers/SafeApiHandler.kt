package com.bookshelf.app.core.network.exceptionhandlers

import com.bookshelf.app.core.network.NetworkResponse
import retrofit2.Response

/**
 * Executes a safe API call by invoking the specified function, handling potential exceptions
 * and providing a NetworkResponse that represents the result of the API call.
 *
 * @param T The expected type of the response.
 * @param function The suspend function that makes the API call.
 * @return A [NetworkResponse] that represents the outcome of the API call.
 */
suspend inline fun <reified T> safeApiCall(crossinline function: suspend () -> Response<T>): NetworkResponse<T> {
    return try {
        // Execute the API call and handle the response or error.
        function.invoke().run {
            if (isSuccessful && body() != null) {
                // Successful API response with a non-null body.
                NetworkResponse.Success(body()!!)
            } else {
                // Handle error response.
                ErrorHandler.parseError(errorBody(), code())
            }
        }
    } catch (e: Exception) {
        // Handle exceptions that may occur during the API call.
        e.printStackTrace()
        ErrorHandler.handleException(e)
    }
}
