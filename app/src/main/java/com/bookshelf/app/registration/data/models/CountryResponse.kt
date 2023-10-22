package com.bookshelf.app.registration.data.models

/**
 * Represents a response containing information about countries.
 *
 * @property data A map that associates country names with [Country] objects containing country details.
 */
data class CountryResponse(
    val data: Map<String, Country>
)

/**
 * Represents information about a specific country.
 *
 * @property country The name of the country (nullable).
 * @property region The region or area where the country is located (nullable).
 */
data class Country(
    val country: String? = null,
    val region: String? = null
)