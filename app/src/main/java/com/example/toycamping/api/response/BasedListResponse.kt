package com.example.toycamping.api.response

import com.google.gson.annotations.SerializedName

data class BasedListResponse(
    @SerializedName("response")
    val basedResponse: BasedResponse
)

data class BasedResponse(
    @SerializedName("body")
    val basedListBody: BasedListBody,
    @SerializedName("header")
    val header: Header
)

data class BasedListBody(
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
    val basedListItem: List<BasedListItem>
)

data class BasedListItem(
    val addr1: String,
    val allar: String,
    val animalCmgCl: String,
    val autoSiteCo: String,
    val bizrno: String,
    val brazierCl: String,
    val caravAcmpnyAt: String,
    val caravSiteCo: String,
    val clturEventAt: String,
    val contentId: String,
    val createdtime: String,
    val doNm: String,
    val exprnProgrmAt: String,
    val extshrCo: String,
    val facltNm: String,
    val fireSensorCo: String,
    val frprvtSandCo: String,
    val frprvtWrppCo: String,
    val glampSiteCo: String,
    val gnrlSiteCo: String,
    val induty: String,
    val indvdlCaravSiteCo: String,
    val insrncAt: String,
    val manageNmpr: String,
    val manageSttus: String,
    val mangeDivNm: String,
    val mapX: Double,
    val mapY: Double,
    val modifiedtime: String,
    val operDeCl: String,
    val operPdCl: String,
    val prmisnDe: String,
    val sigunguNm: String,
    val siteBottomCl1: String,
    val siteBottomCl2: String,
    val siteBottomCl3: String,
    val siteBottomCl4: String,
    val siteBottomCl5: String,
    val siteMg1Co: String,
    val siteMg1Vrticl: String,
    val siteMg1Width: String,
    val siteMg2Co: String,
    val siteMg2Vrticl: String,
    val siteMg2Width: String,
    val siteMg3Co: String,
    val siteMg3Vrticl: String,
    val siteMg3Width: String,
    val sitedStnc: String,
    val swrmCo: String,
    val tel: String,
    val toiletCo: String,
    val trlerAcmpnyAt: String,
    val wtrplCo: String,
    val zipcode: String
)