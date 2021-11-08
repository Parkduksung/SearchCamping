package com.example.toycamping.api

import com.example.toycamping.api.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoCampingApi {

    companion object {
        private const val GO_CAMPING_KEY =
            "R2RhdScz9hCftK1V5Ouq1pbZYgwUN5DYGpryUpEaaqb5XbqD0putXtCzB7GQ1y1F2fAzzD5c%2BkT8lD4E3nSQ1g%3D%3D"

        private const val MOBILE_OS = "AND"

        private const val MOBILE_APP = "CarCamping"

        private const val BASE_URL =
            "ServiceKey=$GO_CAMPING_KEY&pageNo=1&numOfRows=2748&MobileOS=$MOBILE_OS&MobileApp=$MOBILE_APP"

        private const val BASEDLIST_URL =
            "basedList?numOfRows=2390&pageNo=1&ServiceKey=$GO_CAMPING_KEY&MobileOS=$MOBILE_OS&MobileApp=$MOBILE_APP"

        private const val LOCATION_LIST_URL =
            "locationBasedList?numOfRows=150&pageNo=1&ServiceKey=$GO_CAMPING_KEY&MobileOS=$MOBILE_OS&MobileApp=$MOBILE_APP"

        private const val SEARCH_LIST_URL =
            "searchList?ServiceKey=$GO_CAMPING_KEY&MobileOS=$MOBILE_OS&MobileApp=$MOBILE_APP"

        private const val IMAGE_LIST_URL =
            "imageList?ServiceKey=$GO_CAMPING_KEY&MobileOS=$MOBILE_OS&MobileApp=$MOBILE_APP"
        private const val TYPE_JSON = "json"
    }

    @GET(BASEDLIST_URL)
    fun getBasedList(
        @Query(value = "_type") type: String = TYPE_JSON
    ): Call<BasedListResponse>


    @GET(LOCATION_LIST_URL)
    fun getLocationList(
        @Query(value = "mapX") mapX: Double,
        @Query(value = "mapY") mapY: Double,
        @Query(value = "radius") radius: Int,
        @Query(value = "_type") type: String = TYPE_JSON
    ): Call<LocationBasedListResponse>


    @GET(SEARCH_LIST_URL)
    fun getSearchList(
        @Query("keyword", encoded = true) keyword: String,
        @Query("_type") _type: String = TYPE_JSON
    ): Call<SearchListResponse>


    @GET(IMAGE_LIST_URL)
    fun getImageList(
        @Query(value = "contentId") contentId: String,
        @Query(value = "_type") type: String = TYPE_JSON
    ): Call<ImageListResponse>
}