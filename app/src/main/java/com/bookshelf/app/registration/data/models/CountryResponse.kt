package com.bookshelf.app.registration.data.models

data class CountryResponse(
    val data: Map<String, Country>
)

data class Country(
    val country: String? = null,
    val region: String? = null
)