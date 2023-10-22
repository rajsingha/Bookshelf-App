package com.bookshelf.app.registration.domain.usecase

import android.content.Context
import com.bookshelf.app.registration.data.tables.CountryEntity
import com.bookshelf.app.registration.data.tables.UserCredsEntity
import com.bookshelf.app.registration.data.tables.dao.CountryDao
import com.bookshelf.app.registration.data.tables.dao.UserCredsDao
import com.bookshelf.app.registration.domain.repository.RegistrationRepo
import com.bookshelf.app.registration.domain.repository.SessionRepository


/**
 * Use case class responsible for handling registration-related operations and business logic.
 *
 * @param repo The repository for registration-related data operations.
 * @param userCredsDao The data access object (DAO) for user credentials operations.
 * @param countryDao The DAO for country data operations.
 * @param sessionRepo The repository for user session data operations.
 * @param context The Android application context.
 */
class RegistrationUseCase(
    private val repo: RegistrationRepo,
    private val userCredsDao: UserCredsDao,
    private val countryDao: CountryDao,
    val sessionRepo: SessionRepository,
    val context: Context
) {
    /**
     * Inserts user credentials data into the database.
     *
     * @param userCredsEntity The user credentials entity to be inserted.
     */
    suspend fun insertUserCreds(userCredsEntity: UserCredsEntity) =
        userCredsDao.insertUser(userCredsEntity)

    /**
     * Retrieves user information by user email.
     *
     * @param userEmail The email of the user to search for.
     */
    suspend fun getUserByUserEmail(userEmail: String) = userCredsDao.getUserByUserEmail(userEmail)

    /**
     * Retrieves a list of countries from the repository.
     *
     * @return A [Flow] emitting a [NetworkResponse] containing a [CountryResponse] object.
     */
    suspend fun getCountryList() = repo.getCountryList()

    /**
     * Retrieves information about the user's IP address from the repository.
     *
     * @return A [Flow] emitting a [NetworkResponse] containing an [IPAddressResponse] object.
     */
    suspend fun getIpInfo() = repo.getIpInfo()

    /**
     * Retrieves the count of countries in the database.
     */
    suspend fun getCountryRowCount() = countryDao.getRowCount()

    /**
     * Retrieves a list of countries from the database.
     *
     * @return A list of [CountryEntity] objects representing countries.
     */
    suspend fun getCountriesFromDb() = countryDao.getAllCountries()

    /**
     * Inserts a country entity into the database.
     *
     * @param countryEntity The country entity to be inserted.
     */
    suspend fun insertCountriesInDb(countryEntity: CountryEntity) =
        countryDao.insertCountry(countryEntity)
}
