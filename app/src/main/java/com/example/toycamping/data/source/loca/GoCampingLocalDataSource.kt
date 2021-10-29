package com.example.toycamping.data.source.loca

import com.example.toycamping.room.entity.CampingEntity
import com.example.toycamping.utils.Result

interface GoCampingLocalDataSource {

    suspend fun getCampingData(
        name: String
    ): Result<CampingEntity>

    suspend fun isExistCampingEntity(
        name: String
    ): Boolean

}