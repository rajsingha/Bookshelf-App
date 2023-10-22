package com.bookshelf.app.registration.data.models


import com.google.gson.annotations.SerializedName

data class IPAdressResponse(
    @SerializedName("as")
    var asX: String? = null,
    @SerializedName("city")
    var city: String? = null,
    @SerializedName("country")
    var country: String? = null,
    @SerializedName("countryCode")
    var countryCode: String? = null,
    @SerializedName("isp")
    var isp: String? = null,
    @SerializedName("lat")
    var lat: Double? = null,
    @SerializedName("lon")
    var lon: Double? = null,
    @SerializedName("org")
    var org: String? = null,
    @SerializedName("query")
    var query: String? = null,
    @SerializedName("region")
    var region: String? = null,
    @SerializedName("regionName")
    var regionName: String? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("timezone")
    var timezone: String? = null,
    @SerializedName("zip")
    var zip: String? = null
)