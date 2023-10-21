package com.bookshelf.app.registration.domain.repository

import com.bookshelf.app.registration.data.tables.SessionEntity
import com.bookshelf.app.registration.data.tables.dao.SessionDao
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class SessionRepository @Inject constructor(private val sessionDao: SessionDao) {
    val session: Flow<SessionEntity?> = sessionDao.observeSession()

    suspend fun saveSession(email: String) {
        sessionDao.insertSession(SessionEntity(sessionId = generateSessionId(), email = email))
    }

    suspend fun clearSession() {
        sessionDao.clearSession()
    }

    private fun generateSessionId(): String {
        val uuid = UUID.randomUUID()
        val timestamp = System.currentTimeMillis()
        return "$uuid-$timestamp"
    }
}
