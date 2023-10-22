package com.bookshelf.app.dashboard.data.tables.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bookshelf.app.dashboard.data.tables.BooksEntity


@Dao
interface BooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(booksEntity: BooksEntity)

    @Query("SELECT * FROM tbl_books_data")
    suspend fun getAllBooks(): MutableList<BooksEntity>?

    @Query("SELECT COUNT(*) FROM tbl_books_data")
    suspend fun getBooksRowCount(): Int

    @Query("SELECT * FROM tbl_books_data WHERE isFavourite = 1")
    suspend fun getFavouriteBooks(): MutableList<BooksEntity>?

    @Query("SELECT * FROM tbl_books_data WHERE title LIKE :searchQuery")
    fun searchBooksAlike(searchQuery: String): MutableList<BooksEntity>?

    @Query("UPDATE tbl_books_data SET isFavourite = 1 WHERE uid = :uid")
    suspend fun markBookAsFavourite(uid: String)

    @Query("UPDATE tbl_books_data SET isFavourite = 0 WHERE uid = :uid")
    suspend fun unMarkFavouriteBook(uid: String)

    @Query("DELETE FROM tbl_books_data")
    suspend fun clearBooksData()
}