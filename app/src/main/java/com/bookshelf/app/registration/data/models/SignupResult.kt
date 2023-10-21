package com.bookshelf.app.registration.data.models

sealed class SignupResult {
    object Success : SignupResult()
    object UsernameTaken : SignupResult()
    object Failure : SignupResult()
}
