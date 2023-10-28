package com.bookshelf.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bookshelf.app.dashboard.data.tables.BooksEntity
import com.bookshelf.app.dashboard.data.tables.BooksMetaDataEntity
import com.bookshelf.app.dashboard.data.tables.dao.BooksDao
import com.bookshelf.app.dashboard.data.tables.dao.BooksMetaDataDao
import com.bookshelf.app.registration.data.tables.CountryEntity
import com.bookshelf.app.registration.data.tables.SessionEntity
import com.bookshelf.app.registration.data.tables.UserCredsEntity
import com.bookshelf.app.registration.data.tables.dao.CountryDao
import com.bookshelf.app.registration.data.tables.dao.SessionDao
import com.bookshelf.app.registration.data.tables.dao.UserCredsDao

/**
 * AppDatabase is a Room database that provides access to various Data Access Object (DAO) instances for
 * different entities in the application. It is used for local data storage and retrieval.
 */
@Database(
    entities = [
        UserCredsEntity::class,
        SessionEntity::class,
        CountryEntity::class,
        BooksEntity::class,
        BooksMetaDataEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the DAO for UserCredsEntity.
     *
     * @return An instance of the UserCredsDao for performing database operations related to user credentials.
     */
    abstract fun userCredsDao(): UserCredsDao

    /**
     * Provides access to the DAO for SessionEntity.
     *
     * @return An instance of the SessionDao for managing user sessions in the database.
     */
    abstract fun userSessionDao(): SessionDao

    /**
     * Provides access to the DAO for CountryEntity.
     *
     * @return An instance of the CountryDao for working with country-related data in the database.
     */
    abstract fun countryDao(): CountryDao

    /**
     * Provides access to the DAO for BooksEntity.
     *
     * @return An instance of the BooksDao for performing database operations related to books data.
     */
    abstract fun booksDataDao(): BooksDao

    /**
     * Provides access to the DAO for BooksMetaDataEntity.
     *
     * @return An instance of the BooksMetaDataDao for performing database operations related to user books data.
     */
    abstract fun booksMetadataDao(): BooksMetaDataDao
}
