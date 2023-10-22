package com.bookshelf.app.registration.data.models


import com.google.gson.annotations.SerializedName

data class IPAddressResponse(
    @SerializedName("country")
    var country: String? = null,
)