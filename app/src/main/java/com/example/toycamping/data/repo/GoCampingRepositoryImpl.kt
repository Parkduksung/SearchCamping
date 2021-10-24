package com.example.toycamping.data.repo

import com.example.toycamping.api.response.*
import com.example.toycamping.data.source.loca.GoCampingLocalDataSource
import com.example.toycamping.data.source.loca.GoCampingLocalDataSourceImpl
import com.example.toycamping.data.source.remote.GoCampingRemoteDataSource
import org.koin.java.KoinJavaComponent.inject

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
}