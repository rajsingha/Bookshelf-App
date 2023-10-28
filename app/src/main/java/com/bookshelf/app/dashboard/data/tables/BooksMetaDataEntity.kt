package com.bookshelf.app.dashboard.data.tables

import androidx.room.Entity
import androidx.room.Index

/**
 * Entity class representing book metadata, including information about favorites and tags.
 * This entity has a composite primary key for "uid" and "userEmailId" to ensure uniqueness.
 *
 * @param uid The unique identifier for the book.
 * @param userEmailId The email ID of the user associated with the book metadata.
 * @param isFavourite An integer representing whether the book is marked as a favorite (default is 0).
 * @param tags A string representing tags associated with the book (default is null).
 */
@Entity(
    tableName = "tbl_books_metadata",
    primaryKeys = ["uid", "userEmailId"],
    indices = [Index(value = ["uid", "userEmailId"], unique = true)]
)
data class BooksMetaDataEntity(
    val uid: String,
    val userEmailId: String,
    var isFavourite: Int? = 0,
    val tags: String? = null
)