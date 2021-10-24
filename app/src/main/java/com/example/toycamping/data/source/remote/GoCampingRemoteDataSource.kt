package com.example.toycamping.data.source.remote

import com.example.toycamping.api.response.*

interface GoCampingRemoteDataSource {

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
}