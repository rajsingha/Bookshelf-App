package com.bookshelf.app.registration.domain.repository

import com.bookshelf.app.registration.data.tables.SessionEntity
import com.bookshelf.app.registration.data.tables.dao.SessionDao
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject


/**
 * Repository responsible for managing user session data and interactions.
 *
 * @param sessionDao The data access object (DAO) for user session operations.
 */
class SessionRepository @Inject constructor(private val sessionDao: SessionDao) {
    /**
     * A [Flow] that observes the user's session state, emitting a nullable [SessionEntity].
     */
    val session: Flow<SessionEntity?> = sessionDao.observeSession()

    /**
     * Saves a new user session in the database.
     *
     * @param email The user's email associated with the session.
     */
    suspend fun saveSession(email: String) {
        sessionDao.insertSession(SessionEntity(sessionId = generateSessionId(), email = email))
    }

    /**
     * Clears the user's session by removing it from the database.
     */
    suspend fun clearSession() {
        sessionDao.clearSession()
    }

    /**
     * Generates a unique session identifier based on a combination of UUID and timestamp.
     *
     * @return A unique session identifier.
     */
    private fun generateSessionId(): String {
        val uuid = UUID.randomUUID()
        val timestamp = System.currentTimeMillis()
        return "$uuid-$timestamp"
    }
}
