package com.bookshelf.app.registration.domain.usecase

import android.content.Context
import com.bookshelf.app.registration.data.tables.CountryEntity
import com.bookshelf.app.registration.data.tables.UserCredsEntity
import com.bookshelf.app.registration.data.tables.dao.CountryDao
import com.bookshelf.app.registration.data.tables.dao.UserCredsDao
import com.bookshelf.app.registration.domain.repository.RegistrationRepo
import com.bookshelf.app.registration.domain.repository.SessionRepository

class RegistrationUseCase(
    private val repo: RegistrationRepo,
    private val userCredsDao: UserCredsDao,
    private val countryDao: CountryDao,
    val sessionRepo: SessionRepository,
    val context: Context
) {
    suspend fun insertUserCreds(userCredsEntity: UserCredsEntity) =
        userCredsDao.insertUser(userCredsEntity)

    suspend fun getUserByUserEmail(userEmail: String) = userCredsDao.getUserByUserEmail(userEmail)

    suspend fun getCountryList() = repo.getCountryList()

    suspend fun getIpInfo() = repo.getIpInfo()
    suspend fun getCountryRowCount() = countryDao.getRowCount()
    suspend fun getCountriesFromDb() = countryDao.getAllCountries()

    suspend fun insertCountriesInDb(countryEntity: CountryEntity) =
        countryDao.insertCountry(countryEntity)
}