package com.bookshelf.app.registration.data.models

/**
 * A sealed class representing the result of a user signup operation.
 */
sealed class SignupResult {

    /**
     * Represents a successful signup result.
     */
    object Success : SignupResult()

    /**
     * Represents a result indicating that the user's email is already taken.
     */
    object UserEmailTaken : SignupResult()

    /**
     * Represents a failure result for the signup operation.
     */
    object Failure : SignupResult()
}
