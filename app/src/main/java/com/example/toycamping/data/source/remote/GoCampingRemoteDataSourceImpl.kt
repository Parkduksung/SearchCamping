package com.example.toycamping.data.source.remote

import com.example.toycamping.api.GoCampingApi
import com.example.toycamping.api.response.BasedListResponse
import com.example.toycamping.api.response.ImageListResponse
import com.example.toycamping.api.response.LocationBasedListResponse
import com.example.toycamping.api.response.SearchListResponse
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class GoCampingRemoteDataSourceImpl :
    GoCampingRemoteDataSource {

    private val goCampingApi by inject<GoCampingApi>(GoCampingApi::class.java)

    override fun getBasedList(
        onSuccess: (basedListResponse: BasedListResponse) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    ) {
        goCampingApi.getBasedList().enqueue(object : Callback<BasedListResponse> {
            override fun onResponse(
                call: Call<BasedListResponse>,
                response: Response<BasedListResponse>
            ) {
                response.body()?.let(onSuccess)
            }

            override fun onFailure(call: Call<BasedListResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

    override fun getLocationList(
        longitude: Double,
        latitude: Double,
        radius: Int,
        onSuccess: (locationBasedListResponse: LocationBasedListResponse) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    ) {
        goCampingApi.getLocationList(longitude, latitude, radius)
            .enqueue(object : Callback<LocationBasedListResponse> {
                override fun onResponse(
                    call: Call<LocationBasedListResponse>,
                    response: Response<LocationBasedListResponse>
                ) {
                    response.body()?.let(onSuccess)
                }

                override fun onFailure(call: Call<LocationBasedListResponse>, t: Throwable) {
                    onFailure(t)
                }
            })
    }

    override fun getSearchList(
        keyword: String,
        onSuccess: (searchListResponse: SearchListResponse) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    ) {

        val toEncodingKeyword = URLEncoder.encode(keyword, "UTF-8")

        goCampingApi.getSearchList(keyword = toEncodingKeyword)
            .enqueue(object : Callback<SearchListResponse> {
                override fun onResponse(
                    call: Call<SearchListResponse>,
                    response: Response<SearchListResponse>
                ) {
                    response.body()?.let(onSuccess)
                }

                override fun onFailure(call: Call<SearchListResponse>, t: Throwable) {
                    onFailure(t)
                }
            })
    }

    override fun getImageList(
        contentId: String,
        onSuccess: (imageListResponse: ImageListResponse) -> Unit,
        onFailure: (throwable: Throwable) -> Unit
    ) {
        goCampingApi.getImageList(contentId).enqueue(object : Callback<ImageListResponse> {
            override fun onResponse(
                call: Call<ImageListResponse>,
                response: Response<ImageListResponse>
            ) {
                response.body()?.let(onSuccess)
            }

            override fun onFailure(call: Call<ImageListResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }

}