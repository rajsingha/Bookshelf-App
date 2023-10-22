package com.bookshelf.app.registration.data.repository

import com.bookshelf.app.core.network.exceptionhandlers.ErrorHandler
import com.bookshelf.app.core.network.exceptionhandlers.applyCommonSideEffects
import com.bookshelf.app.core.network.exceptionhandlers.safeApiCall
import com.bookshelf.app.registration.data.datasource.RegistrationDataSource
import com.bookshelf.app.registration.domain.repository.RegistrationRepo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Implementation of the [RegistrationRepo] interface that provides registration-related data.
 *
 * @param dataSource The data source responsible for fetching registration data.
 */
class RegistrationRepoImpl @Inject constructor(private val dataSource: RegistrationDataSource) :
    RegistrationRepo {
    /**
     * Retrieves a list of countries from the data source.
     *
     * @return A flow emitting the response containing a list of countries.
     */
    override suspend fun getCountryList() = flow {
        val response =
            safeApiCall { dataSource.getCountries() }
        emit(response)
    }.applyCommonSideEffects().catch { emit(ErrorHandler.handleException(it as Exception)) }

    /**
     * Retrieves information about the user's IP address from the data source.
     *
     * @return A flow emitting the response containing IP address information.
     */
    override suspend fun getIpInfo() = flow {
        val response =
            safeApiCall { dataSource.getIpInfo() }
        emit(response)
    }.applyCommonSideEffects().catch { emit(ErrorHandler.handleException(it as Exception)) }
}






