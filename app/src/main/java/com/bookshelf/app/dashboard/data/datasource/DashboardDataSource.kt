package com.bookshelf.app.dashboard.data.datasource

import com.bookshelf.app.dashboard.data.models.BooksDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface DashboardDataSource {
    @GET
    suspend fun getBooksInfo(@Url url: String = "https://www.jsonkeeper.com/b/CNGI"): Response<BooksDataResponse>
}
