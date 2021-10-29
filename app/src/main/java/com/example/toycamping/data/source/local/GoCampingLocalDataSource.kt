package com.example.toycamping.data.source.local

import com.example.toycamping.room.CampingEntity
import com.example.toycamping.utils.Result

interface GoCampingLocalDataSource {

    suspend fun getAllCampingData(): Result<List<CampingEntity>>

    suspend fun checkExistCampingData(): Boolean

    suspend fun registerCampingData(campingEntity: CampingEntity): Boolean

    suspend fun getCampingData(
        name: String
    ): Result<CampingEntity>

    suspend fun isExistCampingEntity(
        name: String
    ): Boolean

}