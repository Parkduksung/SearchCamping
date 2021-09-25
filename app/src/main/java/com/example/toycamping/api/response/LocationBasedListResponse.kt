package com.example.toycamping.api.response

import com.google.gson.annotations.SerializedName

data class LocationBasedListResponse(
    @SerializedName("response")
    val response: LocationResponse
)


data class LocationResponse(
    @SerializedName("body")
    val body: LocationBody,
    @SerializedName("header")
    val header: Header
)

data class LocationBody(
    @SerializedName("items")
    val items: LocationItems,
    @SerializedName("numOfRows")
    val numOfRows: Int,
    @SerializedName("pageNo")
    val pageNo: Int,
    @SerializedName("totalCount")
    val totalCount: Int
)

data class LocationItems(
    @SerializedName("item")
    val item: List<LocationItem>
)

data class LocationItem(
    val addr1: String,
    val addr2: String,
    val allar: Int,
    val animalCmgCl: String,
    val autoSiteCo: Int,
    val bizrno: String,
    val brazierCl: String,
    val caravAcmpnyAt: String,
    val caravSiteCo: Int,
    val clturEventAt: String,
    val contentId: Int,
    val createdtime: String,
    val doNm: String,
    val eqpmnLendCl: String,
    val exprnProgrmAt: String,
    val extshrCo: Int,
    val facltDivNm: String,
    val facltNm: String,
    val featureNm: String,
    val fireSensorCo: Int,
    val firstImageUrl: String,
    val frprvtSandCo: Int,
    val frprvtWrppCo: Int,
    val glampInnerFclty: String,
    val glampSiteCo: Int,
    val gnrlSiteCo: Int,
    val homepage: String,
    val induty: String,
    val indvdlCaravSiteCo: Int,
    val insrncAt: String,
    val intro: String,
    val lctCl: String,
    val lineIntro: String,
    val manageNmpr: Int,
    val manageSttus: String,
    val mangeDivNm: String,
    val mapX: Double,
    val mapY: Double,
    val modifiedtime: String,
    val operDeCl: String,
    val operPdCl: String,
    val posblFcltyCl: String,
    val prmisnDe: String,
    val resveCl: String,
    val resveUrl: String,
    val sbrsCl: String,
    val sigunguNm: String,
    val siteBottomCl1: Int,
    val siteBottomCl2: Int,
    val siteBottomCl3: Int,
    val siteBottomCl4: Int,
    val siteBottomCl5: Int,
    val siteMg1Co: Int,
    val siteMg1Vrticl: Int,
    val siteMg1Width: Int,
    val siteMg2Co: Int,
    val siteMg2Vrticl: Int,
    val siteMg2Width: Int,
    val siteMg3Co: Int,
    val siteMg3Vrticl: Int,
    val siteMg3Width: Int,
    val sitedStnc: Int,
    val swrmCo: Int,
    val tel: String,
    val themaEnvrnCl: String,
    val toiletCo: Int,
    val trlerAcmpnyAt: String,
    val wtrplCo: Int,
    val zipcode: Int
)