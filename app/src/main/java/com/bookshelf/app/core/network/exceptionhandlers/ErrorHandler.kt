package com.bookshelf.app.core.network.exceptionhandlers


import android.net.sip.SipErrorCode
import com.bookshelf.app.core.network.BaseModel
import com.bookshelf.app.core.network.NetworkResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.ResponseBody
import java.net.ConnectException

object ErrorHandler {
    const val MAX_CHARS = 150
    const val NO_INTERNET_ERROR_CODE = 1
    const val NETWORK_ERROR_MESSAGE =
        "No internet connection, Please check you mobile data or Wi-fi"
    const val PLEASE_TRY_AGAIN = "Failed to connect to the server, Please try after some time"
    const val SOMETHING_WRONG_TRY_AGAIN = "Something went wrong. Please try again later!"
    const val PARSING_ERROR = "PARSING_ERROR"


    inline fun <reified T> handleException(
        exception: Exception
    ): NetworkResponse<T> {
        return when (exception) {
            is NoNetworkException -> {
                NetworkResponse.Error(
                    ApiFailureException(
                        NETWORK_ERROR_MESSAGE,
                        null,
                        NO_INTERNET_ERROR_CODE
                    )
                )
            }

            is ConnectException -> {
                NetworkResponse.Error(
                    ApiFailureException(
                        PLEASE_TRY_AGAIN,
                        null,
                        SipErrorCode.SERVER_ERROR
                    )
                )
            }

            is JsonSyntaxException -> {
                NetworkResponse.Error(
                    ApiFailureException(
                        message = PARSING_ERROR,
                        cause = null,
                        null
                    )
                )
            }

            else -> {
                NetworkResponse.Error(
                    ApiFailureException(
                        exception.localizedMessage,
                        exception,
                        null
                    )
                )
            }
        }
    }

    inline fun <reified T> parseError(errorBody: ResponseBody?, code: Int): NetworkResponse<T> {
        try {
            errorBody?.charStream()?.readText()?.trim()?.let {
                val resp = Gson().fromJson(it, BaseModel::class.java)
                val message = if (resp.message.isNullOrEmpty()) {
                    resp.errorMessage
                } else {
                    resp.message
                }
                return NetworkResponse.Error(
                    ApiFailureException(
                        message = message,
                        code = code,
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return NetworkResponse.Error(
                ApiFailureException(
                    message = PARSING_ERROR,
                    code = code
                )
            )
        }
        errorBody?.charStream()?.readText()?.trim()?.let { message ->
            return NetworkResponse.Error(
                ApiFailureException(
                    message = message.take(MAX_CHARS),
                    code = code
                )
            )
        } ?: return NetworkResponse.Error(
            ApiFailureException(
                message = SOMETHING_WRONG_TRY_AGAIN,
                code = code
            )
        )
    }
}