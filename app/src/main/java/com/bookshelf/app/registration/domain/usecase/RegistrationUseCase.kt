package com.bookshelf.app.registration.domain.usecase

import android.content.Context
import com.bookshelf.app.registration.data.tables.UserCredsEntity
import com.bookshelf.app.registration.data.tables.dao.UserCredsDao
import com.bookshelf.app.registration.domain.repository.SessionRepository

class RegistrationUseCase(
    private val userCredsDao: UserCredsDao,
    val sessionRepo: SessionRepository,
    val context: Context
) {
    suspend fun insertUserCreds(userCredsEntity: UserCredsEntity) = userCredsDao.insertUser(userCredsEntity)

    suspend fun getUserByUserEmail(userEmail: String) = userCredsDao.getUserByUserEmail(userEmail)
}