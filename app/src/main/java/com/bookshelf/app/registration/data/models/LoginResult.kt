package com.bookshelf.app.registration.data.models

import com.bookshelf.app.registration.data.tables.UserCredsEntity

/**
 * A sealed class representing the result of a user login operation.
 */
sealed class LoginResult {

    /**
     * Represents a successful login result.
     *
     * @property user The user credentials entity associated with the successful login.
     */
    data class Success(val user: UserCredsEntity) : LoginResult()

    /**
     * Represents a failure result for the login operation.
     */
    object Failure : LoginResult()
}