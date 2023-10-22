package com.bookshelf.app.core.network

import com.google.gson.annotations.SerializedName

open class BaseModel(
    @SerializedName("message")
    val message: String? = "",
    @SerializedName("error_message")
    val errorMessage: String? = "",
)