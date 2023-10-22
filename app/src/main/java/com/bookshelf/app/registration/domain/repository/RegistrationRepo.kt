package com.bookshelf.app.registration.domain.repository

import com.bookshelf.app.core.network.NetworkResponse
import com.bookshelf.app.registration.data.models.CountryResponse
import com.bookshelf.app.registration.data.models.IPAddressResponse
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the repository for registration-related operations.
 */
interface RegistrationRepo {
    /**
     * Retrieves a list of countries and their details.
     *
     * @return A [Flow] emitting a [NetworkResponse] containing a [CountryResponse] object.
     */
    suspend fun getCountryList(): Flow<NetworkResponse<CountryResponse>>

    /**
     * Retrieves information about the user's IP address.
     *
     * @return A [Flow] emitting a [NetworkResponse] containing an [IPAddressResponse] object.
     */
    suspend fun getIpInfo(): Flow<NetworkResponse<IPAddressResponse>>
}
