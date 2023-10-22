package com.bookshelf.app.dashboard.data.tables

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tbl_books_data")
data class BooksEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uid: String?,
    val image: String?,
    val score: Double?,
    val popularity: Int?,
    val title: String?,
    val publishedChapterDate: Long?,
    val isFavourite: Int = 0,
)