package com.bookshelf.app.dashboard.data.datasource

import com.bookshelf.app.dashboard.data.models.BooksDataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * DashboardDataSource is an interface that defines the data source for retrieving book information.
 * Implementations of this interface are responsible for making network requests to fetch book data.
 */
interface DashboardDataSource {
    /**
     * Fetches book information from a specified URL.
     *
     * @param url The URL from which to retrieve book information. It defaults to a sample URL.
     * @return A [Response] containing book data as a [BooksDataResponse] or an error response.
     */
    @GET
    suspend fun getBooksInfo(@Url url: String = "https://www.jsonkeeper.com/b/CNGI"): Response<BooksDataResponse>
}
