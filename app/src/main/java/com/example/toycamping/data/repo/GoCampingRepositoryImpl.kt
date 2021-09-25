package com.example.toycamping.data.repo

import com.example.toycamping.api.response.BasedListResponse
import com.example.toycamping.api.response.ImageListResponse
import com.example.toycamping.api.response.LocationBasedListResponse
import com.example.toycamping.api.response.SearchListResponse
import com.example.toycamping.data.source.remote.GoCampingRemoteDataSource
import org.koin.java.KoinJavaComponent.inject

class GoCampingRepositoryImpl :
    GoCampingRepository {

    private val goCampingRemoteDataSource by inject<GoCampingRemoteDataSource>(
        GoCampingRemoteDataSource::class.java
    )

    override fun getBasedList(
        onSuccess: (basedListResponse: BasedListResponse) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    ) {
        goCampingRemoteDataSource.getBasedList(onSuccess, onFailure)
    }

    override fun getLocationList(
        mapX: Double,
        mapY: Double,
        radius: Int,
        onSuccess: (locationBasedListResponse: LocationBasedListResponse) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    ) {
        goCampingRemoteDataSource.getLocationList(mapX, mapY, radius, onSuccess, onFailure)
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