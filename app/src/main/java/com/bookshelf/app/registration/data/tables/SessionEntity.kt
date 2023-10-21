package com.bookshelf.app.registration.data.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_user_session")
data class SessionEntity(
    @PrimaryKey
    val id: Long = 0,
    val sessionId: String,
    val userName: String,
)