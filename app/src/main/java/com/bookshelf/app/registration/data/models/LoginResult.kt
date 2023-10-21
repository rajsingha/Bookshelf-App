package com.bookshelf.app.registration.data.models

import com.bookshelf.app.registration.data.tables.UserCredsEntity

sealed class LoginResult {
    data class Success(val user: UserCredsEntity) : LoginResult()
    object Failure : LoginResult()
}