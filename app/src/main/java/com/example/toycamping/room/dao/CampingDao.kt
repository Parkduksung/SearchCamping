package com.example.toycamping.room.dao

import androidx.room.*
import com.example.toycamping.room.entity.CampingEntity

@Dao
interface CampingDao {

    @Query("SELECT * FROM camping_table WHERE `name` = (:name) ")
    fun getCampingEntity(name: String): CampingEntity


    @Query("SELECT * FROM camping_table WHERE `name` = (:name) ")
    fun isExistCampingEntity(name: String): Long
}