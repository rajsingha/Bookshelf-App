package com.bookshelf.app.registration.domain.repository

import com.bookshelf.app.core.network.NetworkResponse
import com.bookshelf.app.registration.data.models.CountryResponse
import kotlinx.coroutines.flow.Flow

interface RegistrationRepo {
    suspend fun getCountryList(): Flow<NetworkResponse<CountryResponse>>
}