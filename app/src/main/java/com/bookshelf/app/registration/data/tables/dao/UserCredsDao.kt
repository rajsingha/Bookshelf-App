package com.bookshelf.app.registration.data.tables.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bookshelf.app.registration.data.tables.UserCredsEntity

@Dao
interface UserCredsDao {
    @Insert
    suspend fun insertUser(userCredsEntity: UserCredsEntity)

    @Query("SELECT * FROM tbl_user_creds WHERE userName = :username")
    suspend fun getUserByUsername(username: String): UserCredsEntity?
}