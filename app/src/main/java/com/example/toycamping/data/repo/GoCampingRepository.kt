package com.example.toycamping.data.repo

import com.example.toycamping.api.response.*
import com.example.toycamping.room.entity.CampingEntity
import com.example.toycamping.utils.Result

interface GoCampingRepository {

    fun getBasedList(
        onSuccess: (basedListResponse: BasedListResponse) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    )

    fun getLocationList(
        longitude: Double,
        latitude: Double,
        radius: Int,
        onSuccess: (locationBasedListResponse: LocationBasedListResponse) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    )

    fun getSearchList(
        keyword: String,
        onSuccess: (searchListResponse: SearchListResponse) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    )

    fun getImageList(
        contentId: String,
        onSuccess: (imageListResponse: ImageListResponse) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    )


    suspend fun getAllCampingData(): Result<List<CampingEntity>>

    suspend fun toggleBookmarkCampingData(
        item: CampingEntity
    ): Result<CampingEntity>

    suspend fun getAllBookmarkList(): Result<List<CampingEntity>>

    suspend fun checkExistCampingData(): Boolean

    suspend fun registerCampingData(campingEntity: CampingEntity): Boolean

    suspend fun getCampingData(
        name: String
    ): Result<CampingEntity>
}