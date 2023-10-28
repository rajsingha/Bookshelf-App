package com.bookshelf.app.dashboard.domain.repository

import com.bookshelf.app.core.network.NetworkResponse
import com.bookshelf.app.dashboard.data.models.BooksDataResponse
import kotlinx.coroutines.flow.Flow

interface DashboardRepo {

    /**
     * Retrieves book information from a data source and provides it as a flow.
     *
     * @return A flow of network responses containing book data.
     */
    suspend fun getBooksInfo(): Flow<NetworkResponse<BooksDataResponse>>
}