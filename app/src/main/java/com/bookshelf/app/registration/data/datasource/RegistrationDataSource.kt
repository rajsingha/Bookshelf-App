package com.bookshelf.app.registration.data.datasource

import com.bookshelf.app.registration.data.models.CountryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface RegistrationDataSource {

    @GET
    suspend fun getCountries(@Url url: String = "https://api.first.org/data/v1/countries"): Response<CountryResponse>
}