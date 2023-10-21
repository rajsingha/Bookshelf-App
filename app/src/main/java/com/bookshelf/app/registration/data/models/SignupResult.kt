package com.bookshelf.app.registration.data.models

sealed class SignupResult {
    object Success : SignupResult()
    object UserEmailTaken : SignupResult()
    object Failure : SignupResult()
}
