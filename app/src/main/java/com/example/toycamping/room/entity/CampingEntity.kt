package com.example.toycamping.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "camping_table")
data class CampingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "like") val like: Boolean,
    @ColumnInfo(name = "homepage") val homepage: String? = null,
    @ColumnInfo(name = "tel") val tel: String? = null,
    @ColumnInfo(name = "latitude") val lat: Double,
    @ColumnInfo(name = "longitude") val log: Double
)