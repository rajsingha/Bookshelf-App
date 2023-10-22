package com.bookshelf.app.core.di

import android.content.Context
import androidx.room.Room
import com.bookshelf.app.core.database.AppDatabase
import com.bookshelf.app.registration.data.tables.dao.CountryDao
import com.bookshelf.app.registration.data.tables.dao.SessionDao
import com.bookshelf.app.registration.data.tables.dao.UserCredsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bookshelf_app_database"
        ).build()
    }

    @Provides
    fun provideUserCredsDao(database: AppDatabase): UserCredsDao {
        return database.userCredsDao()
    }

    @Provides
    fun provideUserSessionDao(database: AppDatabase): SessionDao {
        return database.userSessionDao()
    }

    @Provides
    fun provideCountryDao(database: AppDatabase): CountryDao {
        return database.countryDao()
    }
}