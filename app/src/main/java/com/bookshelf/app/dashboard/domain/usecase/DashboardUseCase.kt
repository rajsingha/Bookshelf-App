package com.bookshelf.app.dashboard.domain.usecase

import android.content.Context
import com.bookshelf.app.dashboard.data.tables.BooksEntity
import com.bookshelf.app.dashboard.data.tables.dao.BooksDao
import com.bookshelf.app.dashboard.domain.repository.DashboardRepo
import com.bookshelf.app.registration.domain.repository.SessionRepository

class DashboardUseCase(
    private val dashboardRepo: DashboardRepo,
    val sessionRepo: SessionRepository,
    val context: Context,
    private val booksDao: BooksDao,

    ) {
    suspend fun getBooksInfoFromApi() = dashboardRepo.getBooksInfo()
    suspend fun insertBook(booksEntity: BooksEntity) = booksDao.insertBooks(booksEntity)

    suspend fun getBooksRowCount() = booksDao.getBooksRowCount()

    suspend fun searchBook(title: String): MutableList<BooksEntity>? =
        booksDao.searchBooksAlike("%$title%")

    suspend fun markBookAsFavourite(uid: String) = booksDao.markBookAsFavourite(uid)

    suspend fun unMarkFavouriteBook(uid: String) = booksDao.unMarkFavouriteBook(uid)

    suspend fun getAllBooksFromDB(): MutableList<BooksEntity>? = booksDao.getAllBooks()

    suspend fun getFavouriteBooks(): MutableList<BooksEntity>? = booksDao.getFavouriteBooks()
}