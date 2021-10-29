package com.example.toycamping.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CampingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun registerCampingEntity(excelList: CampingEntity): Long

    @Query("SELECT * FROM camping_table")
    fun getAll(): List<CampingEntity>

    @Query("SELECT * FROM camping_table WHERE `name` = (:name) ")
    fun getCampingEntity(name: String): CampingEntity


    @Query("SELECT * FROM camping_table WHERE `name` = (:name) ")
    fun isExistCampingEntity(name: String): Long
}