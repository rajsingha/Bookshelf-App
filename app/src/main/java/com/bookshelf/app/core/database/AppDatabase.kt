package com.bookshelf.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bookshelf.app.registration.data.tables.SessionEntity
import com.bookshelf.app.registration.data.tables.UserCredsEntity
import com.bookshelf.app.registration.data.tables.dao.SessionDao
import com.bookshelf.app.registration.data.tables.dao.UserCredsDao

@Database(
    entities = [UserCredsEntity::class, SessionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userCredsDao(): UserCredsDao
    abstract fun userSessionDao(): SessionDao
}
