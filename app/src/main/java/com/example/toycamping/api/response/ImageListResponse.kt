package com.example.toycamping.api.response

import com.google.gson.annotations.SerializedName

data class ImageListResponse(
    @SerializedName("response")
    val imageResponse: ImageResponse
)

data class ImageResponse(
    @SerializedName("body")
    val body: ImageBody,
    @SerializedName("header")
    val header: Header
)

data class ImageBody(
    @SerializedName("items")
    val items: ImageItems,
    @SerializedName("numOfRows")
    val numOfRows: Int,
    @SerializedName("pageNo")
    val pageNo: Int,
    @SerializedName("totalCount")
    val totalCount: Int
)

data class ImageItems(
    @SerializedName("item")
    val item: List<ImageItem>
)

data class ImageItem(
    @SerializedName("contentId")
    val contentId: Int,
    @SerializedName("createdtime")
    val createdtime: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("modifiedtime")
    val modifiedtime: String,
    @SerializedName("serialnum")
    val serialnum: Int
)