package com.bookshelf.app.dashboard.data.tables

import androidx.room.Embedded

/**
 * Data class representing a combination of book-related data and its associated metadata.
 * The properties are annotated with @Embedded to specify prefixes for mapping from related entities.
 *
 * @param book The book entity with its properties (e.g., title, author).
 * @param metadata The metadata entity with properties (e.g., isFavourite, tags).
 */
data class BookWithMetadata(
    @Embedded(prefix = "book_") var book: BooksEntity? = null,
    @Embedded(prefix = "metadata_") var metadata: BooksMetaDataEntity? = null
)
