package com.bookshelf.app.dashboard.data.repository

import com.bookshelf.app.core.network.exceptionhandlers.ErrorHandler
import com.bookshelf.app.core.network.exceptionhandlers.applyCommonSideEffects
import com.bookshelf.app.core.network.exceptionhandlers.safeApiCall
import com.bookshelf.app.dashboard.data.datasource.DashboardDataSource
import com.bookshelf.app.dashboard.domain.repository.DashboardRepo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DashboardRepoImpl @Inject constructor(private val dataSource: DashboardDataSource) :
    DashboardRepo {
    override suspend fun getBooksInfo() = flow {
        val response =
            safeApiCall { dataSource.getBooksInfo() }
        emit(response)
    }.applyCommonSideEffects().catch { emit(ErrorHandler.handleException(it as Exception)) }

}
