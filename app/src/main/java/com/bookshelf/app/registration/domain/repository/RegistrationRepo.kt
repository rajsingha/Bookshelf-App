package com.bookshelf.app.registration.domain.repository

import com.bookshelf.app.core.network.NetworkResponse
import com.bookshelf.app.registration.data.models.CountryResponse
import com.bookshelf.app.registration.data.models.IPAdressResponse
import kotlinx.coroutines.flow.Flow

interface RegistrationRepo {
    suspend fun getCountryList(): Flow<NetworkResponse<CountryResponse>>

    suspend fun getIpInfo(): Flow<NetworkResponse<IPAdressResponse>>
}