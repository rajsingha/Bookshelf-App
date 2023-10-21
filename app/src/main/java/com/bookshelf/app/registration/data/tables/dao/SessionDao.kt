package com.bookshelf.app.registration.data.tables.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bookshelf.app.registration.data.tables.SessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionEntity)

    @Query("SELECT * FROM tbl_user_session LIMIT 1")
    fun observeSession(): Flow<SessionEntity?>

    @Query("DELETE FROM tbl_user_session")
    suspend fun clearSession()
}
