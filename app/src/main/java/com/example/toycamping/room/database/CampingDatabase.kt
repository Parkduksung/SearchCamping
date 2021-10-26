package com.example.toycamping.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.toycamping.room.dao.CampingDao
import com.example.toycamping.room.entity.CampingEntity

@Database(entities = [CampingEntity::class], version = 2)
abstract class CampingDatabase : RoomDatabase(){

    abstract fun campingDao(): CampingDao
}