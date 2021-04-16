package com.example.locationfinder.db

import androidx.paging.DataSource
import androidx.room.*

/**
 * McLocationDao
 */
@Dao
interface McLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveLocation(mcItemEntity: McLocationItemEntity): Long

    @Delete
    fun deleteLocation(mcItemEntity: McLocationItemEntity): Int

    @Query("select * from item_db")
    fun getSavedLocation(): DataSource.Factory<Int, McLocationItemEntity>

    @Query("Delete from item_db")
    fun deleteAllSavedLocation(): Int

    @Update
    fun updateLocation(mcItemEntity: McLocationItemEntity): Int

    @Query("select * from item_db where entity_category_group_code = :category")
    fun getFilteredLocation(category: String): DataSource.Factory<Int, McLocationItemEntity>
}