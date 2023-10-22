package com.bookshelf.app.registration.data.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_country")
data class CountryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val country: String
)