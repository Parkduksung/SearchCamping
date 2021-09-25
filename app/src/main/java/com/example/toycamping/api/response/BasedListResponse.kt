package com.example.toycamping.api.response

import com.google.gson.annotations.SerializedName

data class BasedListResponse(
    @SerializedName("response")
    val response: BasedResponse
)

data class BasedResponse(
    @SerializedName("body")
    val body: BasedBody,
    @SerializedName("header")
    val header: Header
)

data class BasedBody(
    @SerializedName("items")
    val basedListItems: BasedListItems,
    @SerializedName("numOfRows")
    val numOfRows: Int,
    @SerializedName("pageNo")
    val pageNo: Int,
    @SerializedName("totalCount")
    val totalCount: Int
)

data class BasedListItems(
    @SerializedName("item")
    val item: List<BasedListItem>
)


data class BasedListItem(
    val addr1: String,
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
    val exprnProgrmAt: String,
    val extshrCo: Int,
    val facltNm: String,
    val fireSensorCo: Int,
    val frprvtSandCo: Int,
    val frprvtWrppCo: Int,
    val glampSiteCo: Int,
    val gnrlSiteCo: Int,
    val induty: String,
    val indvdlCaravSiteCo: Int,
    val insrncAt: String,
    val manageNmpr: Int,
    val manageSttus: String,
    val mangeDivNm: String,
    val mapX: Double,
    val mapY: Double,
    val modifiedtime: String,
    val operDeCl: String,
    val operPdCl: String,
    val prmisnDe: String,
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
    val toiletCo: Int,
    val trlerAcmpnyAt: String,
    val wtrplCo: Int,
    val zipcode: Int
)