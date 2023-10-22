package com.bookshelf.app.core.network.exceptionhandlers

import java.io.IOException

/**
 * Custom exception class which will let the user if no internet available
 * */
open class NoNetworkException : IOException() {

    override fun getLocalizedMessage(): String? = "No Internet Connection"
}