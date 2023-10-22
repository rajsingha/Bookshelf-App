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

fun <T : Any> Flow<NetworkResponse<T>>.applyCommonSideEffects() =
    retryWhen { cause, attempt ->
        when {
            (cause is IOException && attempt < MAX_RETRIES) -> {
                delay(getBackoffDelay(attempt))
                true
            }

            else -> {
                false
            }
        }
    }.onStart { emit(NetworkResponse.Loading(true)) }
        .onCompletion { emit(NetworkResponse.Loading(false)) }

fun <T : Any> Flow<NetworkResponse<T>>.applyCommonSideEffects(forPaging: Boolean) =
    retryWhen { cause, attempt ->
        when {
            (cause is IOException && attempt < MAX_RETRIES) -> {
                delay(getBackoffDelay(attempt))
                true
            }

            else -> {
                false
            }
        }
    }.onStart {
        if (!forPaging) {
            emit(NetworkResponse.Loading(true))
        }
    }.onCompletion {
        if (!forPaging) {
            emit(NetworkResponse.Loading(false))
        }
    }