package com.example.toycamping.data.repo

import com.example.toycamping.api.response.*
import com.example.toycamping.data.source.loca.GoCampingLocalDataSource
import com.example.toycamping.data.source.remote.GoCampingRemoteDataSource
import com.example.toycamping.room.entity.CampingEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import com.example.toycamping.utils.Result

class GoCampingRepositoryImpl :
    GoCampingRepository {

    private val goCampingRemoteDataSource by inject<GoCampingRemoteDataSource>(
        GoCampingRemoteDataSource::class.java
    )

    private val goCampingLocalDataSource by inject<GoCampingLocalDataSource>(
        GoCampingLocalDataSource::class.java
    )

    override fun getBasedList(
        onSuccess: (basedListResponse: BasedListResponse) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    ) {
        goCampingRemoteDataSource.getBasedList(onSuccess, onFailure)
    }

    override fun getLocationList(
        longitude: Double,
        latitude: Double,
        radius: Int,
        onSuccess: (locationBasedListResponse: LocationBasedListResponse) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    ) {
        goCampingRemoteDataSource.getLocationList(longitude, latitude, radius, onSuccess, onFailure)
    }

    override fun getSearchList(
        keyword: String,
        onSuccess: (searchListResponse: SearchListResponse) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    ) {
        goCampingRemoteDataSource.getSearchList(keyword, onSuccess, onFailure)
    }

    override fun getImageList(
        contentId: String,
        onSuccess: (imageListResponse: ImageListResponse) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    ) {
        goCampingRemoteDataSource.getImageList(contentId, onSuccess, onFailure)
    }




    override suspend fun getAllCampingData(): Result<List<CampingEntity>> =
        withContext(Dispatchers.IO) {
            return@withContext goCampingLocalDataSource.getAllCampingData()
        }

    override suspend fun toggleBookmarkCampingData(item: CampingEntity): Result<CampingEntity> =
        withContext(Dispatchers.IO) {
            return@withContext goCampingLocalDataSource.toggleBookmarkCampingData(item)
        }

    override suspend fun getAllBookmarkList(): Result<List<CampingEntity>> =
        withContext(Dispatchers.IO) {
            return@withContext goCampingLocalDataSource.getAllBookmarkList()
        }

    override suspend fun checkExistCampingData(): Boolean = withContext(Dispatchers.IO) {
        return@withContext goCampingLocalDataSource.checkExistCampingData()
    }

    override suspend fun registerCampingData(campingEntity: CampingEntity): Boolean =
        withContext(Dispatchers.IO) {
            return@withContext goCampingLocalDataSource.registerCampingData(campingEntity)
        }

    override suspend fun getCampingData(
        name: String
    ): Result<CampingEntity> = withContext(Dispatchers.IO) {
        return@withContext goCampingLocalDataSource.getCampingData(name)
    }
}