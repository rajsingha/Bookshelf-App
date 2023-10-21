package com.bookshelf.app.registration.presentation.utils

import com.bookshelf.app.registration.data.models.SessionResult
import com.bookshelf.app.registration.data.tables.SessionEntity
import com.bookshelf.app.registration.domain.repository.SessionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SessionManager @Inject constructor(private val sessionRepository: SessionRepository) {
    private val session: Flow<SessionEntity?> = sessionRepository.session
    private val sessionExpirationChannel = Channel<SessionResult>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            session.collect {
                withContext(Dispatchers.Main.immediate){
                    when {
                        (it != null && it.email.isNotEmpty()) -> {
                            sessionExpirationChannel.send(SessionResult.Active(it))
                        }

                        else -> {
                            sessionExpirationChannel.send(SessionResult.NotActive)
                        }
                    }
                }
            }
        }
    }

    fun sessionObserver() = sessionExpirationChannel.receiveAsFlow()

    fun clearSession() {
        runBlocking {
            sessionRepository.clearSession()
        }
    }

    fun saveSession(email: String) {
        runBlocking {
            sessionRepository.saveSession(email)
        }
    }
}
