package com.bookshelf.app.registration.data.models

import com.bookshelf.app.registration.data.tables.SessionEntity

/**
 * A sealed class representing the result of a session operation.
 */
sealed class SessionResult {

    /**
     * Represents an active user session result.
     *
     * @property userSession The user's active session entity.
     */
    data class Active(val userSession: SessionEntity) : SessionResult()

    /**
     * Represents a result indicating that the user session is not active.
     */
    object NotActive : SessionResult()
}