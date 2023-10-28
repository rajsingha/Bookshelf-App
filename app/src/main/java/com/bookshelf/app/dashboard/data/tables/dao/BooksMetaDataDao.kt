package com.bookshelf.app.dashboard.data.tables.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bookshelf.app.dashboard.data.tables.BooksMetaDataEntity

/**
 * Data Access Object (DAO) interface for managing book metadata-related data in the database.
 */
@Dao
interface BooksMetaDataDao {
    /**
     * Inserts book metadata into the database. If a conflict is detected, it replaces the existing data.
     *
     * @param metadata The metadata of the book to be inserted or replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookMetadata(metadata: BooksMetaDataEntity)

    /**
     * Retrieves all book metadata entries from the database.
     *
     * @return A list of all book metadata entries stored in the database.
     */
    @Query("SELECT * FROM tbl_books_metadata")
    suspend fun getAllBooksMetadata(): MutableList<BooksMetaDataEntity>

    /**
     * Updates book metadata in the database.
     *
     * @param metadata The updated metadata to replace the existing entry.
     */
    @Update
    suspend fun updateBookMetadata(metadata: BooksMetaDataEntity)

    /**
     * Retrieves book metadata entries associated with a specific user's email.
     *
     * @param email The email of the user for whom to retrieve book metadata.
     * @return A list of book metadata entries associated with the specified user.
     */
    @Query("SELECT * FROM tbl_books_metadata WHERE userEmailId = :email")
    suspend fun getBooksMetadataForUser(email: String): MutableList<BooksMetaDataEntity>

    /**
     * Retrieves all book metadata entries marked as favorites for a specific user.
     *
     * @param email The email of the user for whom to retrieve favorite book metadata entries.
     * @return A list of book metadata entries marked as favorites for the specified user, or null if there are none.
     */
    @Query("SELECT * FROM tbl_books_metadata WHERE userEmailId = :email AND isFavourite = 1")
    suspend fun getAllFavoriteBooksForUser(email: String): MutableList<BooksMetaDataEntity>?

    /**
     * Clears all book metadata data from the database.
     */
    @Query("DELETE FROM tbl_books_metadata")
    suspend fun clearBooksData()
}
