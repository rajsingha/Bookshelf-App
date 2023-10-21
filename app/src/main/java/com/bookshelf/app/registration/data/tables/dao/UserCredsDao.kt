package com.bookshelf.app.registration.data.tables.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bookshelf.app.registration.data.tables.UserCredsEntity

@Dao
interface UserCredsDao {
    @Insert
    suspend fun insertUser(userCredsEntity: UserCredsEntity)

    @Query("SELECT * FROM tbl_user WHERE email = :email")
    suspend fun getUserByUserEmail(email: String): UserCredsEntity?
}