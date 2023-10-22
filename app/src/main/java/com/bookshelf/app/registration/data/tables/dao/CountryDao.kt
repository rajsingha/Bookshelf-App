package com.bookshelf.app.registration.data.tables.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bookshelf.app.registration.data.tables.CountryEntity

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(countryEntity: CountryEntity)

    @Query("SELECT * FROM tbl_country")
    suspend fun getAllCountries(): MutableList<CountryEntity>?

    @Query("SELECT COUNT(*) FROM tbl_country")
    suspend fun getRowCount(): Int

    @Query("DELETE FROM tbl_country")
    suspend fun clearCountryData()
}
