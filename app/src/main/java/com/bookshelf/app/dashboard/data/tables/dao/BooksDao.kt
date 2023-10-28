package com.bookshelf.app.dashboard.data.tables.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bookshelf.app.dashboard.data.tables.BooksEntity

/**
 * Data Access Object (DAO) interface for managing book-related data in the database.
 */
@Dao
interface BooksDao {
    /**
     * Inserts a list of books into the database.
     *
     * @param booksEntity A list of books to be inserted.
     */
    @Insert
    suspend fun insertBooks(booksEntity: MutableList<BooksEntity>)

    /**
     * Retrieves all books from the database.
     *
     * @return A list of all books stored in the database, or null if there are no books.
     */
    @Query("SELECT * FROM tbl_books_data")
    suspend fun getAllBooks(): MutableList<BooksEntity>?

    /**
     * Retrieves the total number of books in the database.
     *
     * @return The count of books in the database.
     */
    @Query("SELECT COUNT(*) FROM tbl_books_data")
    suspend fun getBooksRowCount(): Int

    /**
     * Retrieves books published in a specific year.
     *
     * @param desiredYear The publication year for filtering books.
     * @return A list of books published in the specified year.
     */
    @Query("SELECT * FROM tbl_books_data WHERE publishedChapterDate = :desiredYear")
    suspend fun getBooksByYear(desiredYear: Long): MutableList<BooksEntity>?

    /**
     * Searches for books with titles or publication years similar to the given search query.
     *
     * @param searchQuery The search query to match against book titles or publication years.
     * @return A list of books that match the search query.
     */
    @Query("SELECT * FROM tbl_books_data WHERE title LIKE :searchQuery OR publishedChapterDate LIKE :searchQuery")
    fun searchBooksAlike(searchQuery: String): MutableList<BooksEntity>?

    /**
     * Clears all book data from the database.
     */
    @Query("DELETE FROM tbl_books_data")
    suspend fun clearBooksData()
}
