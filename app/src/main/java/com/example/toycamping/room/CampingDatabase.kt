package com.example.toycamping.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CampingEntity::class], version = 2)
abstract class CampingDatabase : RoomDatabase(){

    abstract fun campingDao(): CampingDao
}