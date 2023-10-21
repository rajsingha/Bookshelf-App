package com.bookshelf.app.registration.data.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_user")
data class UserCredsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userName: String,
    val email: String,
    val passwordHash: String,
    val country: String
)
