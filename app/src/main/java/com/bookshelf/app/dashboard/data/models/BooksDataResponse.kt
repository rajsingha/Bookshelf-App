package com.bookshelf.app.dashboard.data.models


import com.google.gson.annotations.SerializedName

class BooksDataResponse : ArrayList<BooksDataResponseItem>()
data class BooksDataResponseItem(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("popularity")
    var popularity: Int? = null,
    @SerializedName("publishedChapterDate")
    var publishedChapterDate: Int? = null,
    @SerializedName("score")
    var score: Double? = null,
    @SerializedName("title")
    var title: String? = null,
    var isFavourite: Int = 0
)