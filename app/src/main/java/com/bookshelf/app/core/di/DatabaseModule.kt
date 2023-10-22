package com.bookshelf.app.core.di

import android.content.Context
import androidx.room.Room
import com.bookshelf.app.core.database.AppDatabase
import com.bookshelf.app.dashboard.data.tables.dao.BooksDao
import com.bookshelf.app.registration.data.tables.dao.CountryDao
import com.bookshelf.app.registration.data.tables.dao.SessionDao
import com.bookshelf.app.registration.data.tables.dao.UserCredsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/**
 * Dagger Hilt module responsible for providing database-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides the main application database instance.
     *
     * @param context The Android application context.
     * @return An instance of the AppDatabase.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bookshelf_app_database"
        ).build()
    }

    /**
     * Provides a data access object (DAO) for managing user credentials in the database.
     *
     * @param database The main application database.
     * @return An instance of the UserCredsDao.
     */
    @Provides
    fun provideUserCredsDao(database: AppDatabase): UserCredsDao {
        return database.userCredsDao()
    }

    /**
     * Provides a data access object (DAO) for managing user sessions in the database.
     *
     * @param database The main application database.
     * @return An instance of the SessionDao.
     */
    @Provides
    fun provideUserSessionDao(database: AppDatabase): SessionDao {
        return database.userSessionDao()
    }

    /**
     * Provides a data access object (DAO) for managing country data in the database.
     *
     * @param database The main application database.
     * @return An instance of the CountryDao.
     */
    @Provides
    fun provideCountryDao(database: AppDatabase): CountryDao {
        return database.countryDao()
    }

    /**
     * Provides a data access object (DAO) for managing books data in the database.
     *
     * @param database The main application database.
     * @return An instance of the BooksDao.
     */
    @Provides
    fun provideBooksDao(database: AppDatabase): BooksDao {
        return database.booksDataDao()
    }
}
