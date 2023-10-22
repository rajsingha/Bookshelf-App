package com.bookshelf.app.dashboard.domain.repository

import com.bookshelf.app.core.network.NetworkResponse
import com.bookshelf.app.dashboard.data.models.BooksDataResponse
import kotlinx.coroutines.flow.Flow

interface DashboardRepo {
    suspend fun getBooksInfo(): Flow<NetworkResponse<BooksDataResponse>>
}