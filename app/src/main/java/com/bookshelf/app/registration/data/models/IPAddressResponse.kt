package com.bookshelf.app.registration.data.models


import com.google.gson.annotations.SerializedName

/**
 * Represents a response containing information about an IP address, including the associated country.
 *
 * @property country The name of the country associated with the IP address (nullable).
 */
data class IPAddressResponse(
    @SerializedName("country")
    var country: String? = null,
)