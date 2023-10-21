package com.bookshelf.app.registration.data.models

import com.bookshelf.app.registration.data.tables.SessionEntity


sealed class SessionResult {
    data class Active(val userSession: SessionEntity) : SessionResult()
    object NotActive : SessionResult()
}