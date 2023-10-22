package com.bookshelf.app.registration.data.repository

import com.bookshelf.app.core.network.exceptionhandlers.ErrorHandler
import com.bookshelf.app.core.network.exceptionhandlers.applyCommonSideEffects
import com.bookshelf.app.core.network.exceptionhandlers.safeApiCall
import com.bookshelf.app.registration.data.datasource.RegistrationDataSource
import com.bookshelf.app.registration.domain.repository.RegistrationRepo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegistrationRepoImpl @Inject constructor(private val dataSource: RegistrationDataSource) :
    RegistrationRepo {
    override suspend fun getCountryList() = flow {
        val response =
            safeApiCall { dataSource.getCountries() }
        emit(response)
    }.applyCommonSideEffects().catch { emit(ErrorHandler.handleException(it as Exception)) }

}
