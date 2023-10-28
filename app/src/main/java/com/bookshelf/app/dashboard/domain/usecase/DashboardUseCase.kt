package com.bookshelf.app.dashboard.domain.usecase

import android.content.Context
import com.bookshelf.app.dashboard.data.tables.BookWithMetadata
import com.bookshelf.app.dashboard.data.tables.BooksEntity
import com.bookshelf.app.dashboard.data.tables.BooksMetaDataEntity
import com.bookshelf.app.dashboard.data.tables.dao.BooksDao
import com.bookshelf.app.dashboard.data.tables.dao.BooksMetaDataDao
import com.bookshelf.app.dashboard.domain.repository.DashboardRepo
import com.bookshelf.app.registration.domain.repository.SessionRepository

/**
 * Use case class responsible for handling business logic related to the dashboard.
 * This class serves as an intermediary between the UI and the data sources.
 *
 * @param dashboardRepo The repository for retrieving data from the dashboard API.
 * @param sessionRepo The repository for managing user sessions.
 * @param context The Android application context.
 * @param booksDao The data access object for accessing book-related data.
 * @param booksMetaDataDao The data access object for accessing book metadata.
 */
class DashboardUseCase(
    private val dashboardRepo: DashboardRepo,
    val sessionRepo: SessionRepository,
    val context: Context,
    private val booksDao: BooksDao,
    private val booksMetaDataDao: BooksMetaDataDao
) {

    /**
     * Fetches book information from the API.
     *
     * @return A result containing book information fetched from the API.
     */
    suspend fun getBooksInfoFromApi() = dashboardRepo.getBooksInfo()

    /**
     * Inserts a list of books into the local database.
     *
     * @param booksEntity List of books to be inserted.
     */
    suspend fun insertBook(booksEntity: MutableList<BooksEntity>) =
        booksDao.insertBooks(booksEntity)

    /**
     * Retrieves the total number of books in the database.
     *
     * @return The count of books in the database.
     */
    suspend fun getBooksRowCount() = booksDao.getBooksRowCount()

    /**
     * Searches for books containing a given query and merges them with their metadata.
     *
     * @param query The search query.
     * @return A list of books with their associated metadata.
     */
    suspend fun searchBook(query: String): MutableList<BookWithMetadata> {
        val searchedBooks = booksDao.searchBooksAlike("%$query%")
        val metadataMap = booksMetaDataDao.getAllBooksMetadata()

        // Create a map to efficiently look up metadata by book UID
        val metadataByUid = metadataMap.associateBy { it.uid }

        // Merge books and metadata
        val booksWithMetadata = mutableListOf<BookWithMetadata>()
        if (searchedBooks != null) {
            for (book in searchedBooks) {
                val metadata = metadataByUid[book.uid]
                val mergedBook = BookWithMetadata(book, metadata)
                booksWithMetadata.add(mergedBook)
            }
        }
        return booksWithMetadata
    }

    /**
     * Filters and retrieves a list of books published in a specific year and merges them with their metadata.
     *
     * @param date The publication year for which to filter books.
     * @return A list of books published in the specified year, along with their associated metadata.
     */
    suspend fun filterBooksByYear(date: Long): MutableList<BookWithMetadata> {
        val allBooks = booksDao.getBooksByYear(date)
        val metadataMap = booksMetaDataDao.getAllBooksMetadata()

        // Create a map to efficiently look up metadata by book UID
        val metadataByUid = metadataMap.associateBy { it.uid }

        // Merge books and metadata
        val booksWithMetadata = mutableListOf<BookWithMetadata>()
        if (allBooks != null) {
            for (book in allBooks) {
                val metadata = metadataByUid[book.uid]
                val mergedBook = BookWithMetadata(book, metadata)
                booksWithMetadata.add(mergedBook)
            }
        }

        return booksWithMetadata
    }

    /**
     * Marks a book as a favorite by inserting its metadata into the database.
     *
     * @param bookMetadata The metadata of the book to mark as a favorite.
     */
    suspend fun markBookAsFavorite(bookMetadata: BooksMetaDataEntity) {
        booksMetaDataDao.insertBookMetadata(bookMetadata)
    }

    /**
     * Retrieves all books from the local database and merges them with their metadata.
     *
     * @return A list of books with their associated metadata.
     */
    suspend fun getAllBooksWithMetadata(): MutableList<BookWithMetadata> {
        val allBooks = booksDao.getAllBooks()
        val metadataMap = booksMetaDataDao.getAllBooksMetadata()

        // Create a map to efficiently look up metadata by book UID
        val metadataByUid = metadataMap.associateBy { it.uid }

        // Merge books and metadata
        val booksWithMetadata = mutableListOf<BookWithMetadata>()
        if (allBooks != null) {
            for (book in allBooks) {
                val metadata = metadataByUid[book.uid]
                val mergedBook = BookWithMetadata(book, metadata)
                booksWithMetadata.add(mergedBook)
            }
        }

        return booksWithMetadata
    }

    /**
     * Unmarks a book as a favorite by updating its metadata in the database.
     *
     * @param bookUid The unique identifier of the book to unmark.
     * @param userEmail The email of the user unmarking the book.
     */
    suspend fun unmarkBookAsFavorite(bookUid: String, userEmail: String) {
        val metadata = booksMetaDataDao.getBooksMetadataForUser(userEmail)
            .find { it.uid == bookUid }
        metadata?.apply {
            isFavourite = 0
            booksMetaDataDao.updateBookMetadata(this)
        }
    }

    /**
     * Updates the metadata of a book in the database. If the metadata does not exist, it will be inserted.
     *
     * @param metaDataEntity The metadata of the book to be updated or inserted.
     */
    suspend fun updateBookMetaData(metaDataEntity: BooksMetaDataEntity) =
        booksMetaDataDao.insertBookMetadata(metaDataEntity)

    /**
     * Retrieves a list of books marked as favorites for a specific user and merges them with their metadata.
     *
     * @param emailId The email ID of the user for whom to retrieve favorite books.
     * @return A list of books marked as favorites for the user, along with their associated metadata.
     */
    suspend fun getAllFavoriteBooksForUser(emailId: String): MutableList<BookWithMetadata>? {
        val metadataList = booksMetaDataDao.getAllFavoriteBooksForUser(emailId)
        val allBooks = booksDao.getAllBooks()
        val favoriteBooks = mutableListOf<BookWithMetadata>()

        if (allBooks != null) {
            for (book in allBooks) {
                // Find the corresponding metadata entry for the book
                val metadata = metadataList?.find { it.uid == book.uid }

                // If metadata exists, create a BookWithMetadata instance
                if (metadata != null) {
                    val bookWithMetadata = BookWithMetadata(book, metadata)
                    favoriteBooks.add(bookWithMetadata)
                }
            }
        }

        return favoriteBooks
    }
}
