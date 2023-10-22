package com.bookshelf.app.core.network.exceptionhandlers

import com.bookshelf.app.core.network.NetworkResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

const val MAX_RETRIES = 3L
private const val INITIAL_BACKOFF = 2000L

fun getBackoffDelay(attempt: Long) = INITIAL_BACKOFF * (attempt + 1)

/**
 * some common side-effects to your flow to avoid repeating commonly used
 * logic across the app.
 */

/**
 * Apply common side effects to a flow of NetworkResponse for non-paging network requests.
 * This function handles retries, loading state management, and error handling.
 *
 * @return A modified flow with added side effects.
 */
fun <T : Any> Flow<NetworkResponse<T>>.applyCommonSideEffects() =
    retryWhen { cause, attempt ->
        when {
            // Retry when an IOException occurs within the maximum retry limit.
            (cause is IOException && attempt < MAX_RETRIES) -> {
                delay(getBackoffDelay(attempt))
                true
            }

            else -> false
        }
    }.onStart {
        // Emit loading state at the beginning of the network request.
        emit(NetworkResponse.Loading(true))
    }.onCompletion {
        // Emit loading state when the network request is completed.
        emit(NetworkResponse.Loading(false))
    }

/**
 * Apply common side effects to a flow of NetworkResponse for paging network requests.
 * This function handles retries, loading state management, and error handling.
 *
 * @param forPaging Indicates whether loading state should be emitted (true) or not (false).
 * @return A modified flow with added side effects.
 */
fun <T : Any> Flow<NetworkResponse<T>>.applyCommonSideEffects(forPaging: Boolean) =
    retryWhen { cause, attempt ->
        when {
            // Retry when an IOException occurs within the maximum retry limit.
            (cause is IOException && attempt < MAX_RETRIES) -> {
                delay(getBackoffDelay(attempt))
                true
            }

            else -> false
        }
    }.onStart {
        if (!forPaging) {
            // Emit loading state at the beginning of the network request (if not for paging).
            emit(NetworkResponse.Loading(true))
        }
    }.onCompletion {
        if (!forPaging) {
            // Emit loading state when the network request is completed (if not for paging).
            emit(NetworkResponse.Loading(false))
        }
    }
