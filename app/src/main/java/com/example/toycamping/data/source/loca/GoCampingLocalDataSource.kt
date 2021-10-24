package com.example.toycamping.data.source.loca

import com.example.toycamping.room.entity.CampingEntity

interface GoCampingLocalDataSource {

    suspend fun getAllCampingData(): Result<List<CampingEntity>>

    suspend fun toggleBookmarkCampingData(
        item: CampingEntity
    ): Result<CampingEntity>

    suspend fun getAllBookmarkList(): Result<List<CampingEntity>>

    suspend fun checkExistCampingData(): Boolean

    suspend fun checkExistCampingData(
        item: CampingEntity
    ): Boolean

    suspend fun registerCampingData(campingEntity: CampingEntity): Boolean
}