package com.bookshelf.app.registration.data.datasource

import com.bookshelf.app.registration.data.models.CountryResponse
import com.bookshelf.app.registration.data.models.IPAddressResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * [RegistrationDataSource] is an interface that defines functions for retrieving registration-related data.
 */
interface RegistrationDataSource {

    /**
     * Retrieves a list of countries from a specified URL.
     *
     * @param url The URL for the country data endpoint. Defaults to "https://api.first.org/data/v1/countries" if not provided.
     * @return A [Response] containing a [CountryResponse] object representing a list of countries and their details.
     */
    @GET
    suspend fun getCountries(@Url url: String = "https://api.first.org/data/v1/countries"): Response<CountryResponse>

    /**
     * Retrieves information about the user's IP address from a specified URL.
     *
     * @param url The URL for the IP address data endpoint. Defaults to "http://ip-api.com/json/" if not provided.
     * @return A [Response] containing an [IPAddressResponse] object representing details about the user's IP address.
     */
    @GET
    suspend fun getIpInfo(@Url url: String = "http://ip-api.com/json/"): Response<IPAddressResponse>
}
