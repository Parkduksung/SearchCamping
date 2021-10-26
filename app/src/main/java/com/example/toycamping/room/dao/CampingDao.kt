package com.example.toycamping.room.dao

import androidx.room.*
import com.example.toycamping.room.entity.CampingEntity

@Dao
interface CampingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun registerCampingEntity(excelList: CampingEntity): Long

    @Query("SELECT * FROM camping_table")
    fun getAll(): List<CampingEntity>


    @Query("UPDATE camping_table SET `like` = (:like) WHERE  name = (:name) AND address = (:address)")
    fun updateBookmarkCampingData(
        name: String,
        address: String,
        like: Boolean
    ): Int

    @Query("SELECT * FROM camping_table WHERE `like` = (:like)")
    fun getBookmarkCampingEntity(like: Boolean = true): List<CampingEntity>

    @Query("SELECT * FROM camping_table WHERE `name` = (:name) ")
    fun getCampingEntity(name: String): CampingEntity


    @Query("SELECT * FROM camping_table WHERE `name` = (:name) ")
    fun isExistCampingEntity(name: String): Long
}