package com.example.toycamping.api.response

import com.example.toycamping.data.model.CampingItem
import com.google.gson.annotations.SerializedName

data class SearchListResponse(
    @SerializedName("response")
    val response: SearchResponse
)

data class SearchResponse(
    @SerializedName("body")
    val body: SearchBody,
    @SerializedName("header")
    val header: Header
)

data class SearchBody(
    @SerializedName("items")
    val items: SearchItems,
    @SerializedName("numOfRows")
    val numOfRows: Int,
    @SerializedName("pageNo")
    val pageNo: Int,
    @SerializedName("totalCount")
    val totalCount: Int
)


data class SearchItems(
    @SerializedName("item")
    val item: SearchItem
)

data class SearchItem(
    @SerializedName("addr1")
    val addr1: String,
    @SerializedName("addr2")
    val addr2: String,
    @SerializedName("allar")
    val allar: Int,
    @SerializedName("animalCmgCl")
    val animalCmgCl: String,
    @SerializedName("autoSiteCo")
    val autoSiteCo: Int,
    @SerializedName("bizrno")
    val bizrno: String,
    @SerializedName("brazierCl")
    val brazierCl: String,
    @SerializedName("caravAcmpnyAt")
    val caravAcmpnyAt: String,
    @SerializedName("caravSiteCo")
    val caravSiteCo: Int,
    @SerializedName("clturEvent")
    val clturEvent: String,
    @SerializedName("clturEventAt")
    val clturEventAt: String,
    @SerializedName("contentId")
    val contentId: Int,
    @SerializedName("createdtime")
    val createdtime: String,
    @SerializedName("doNm")
    val doNm: String,
    @SerializedName("eqpmnLendCl")
    val eqpmnLendCl: String,
    @SerializedName("exprnProgrm")
    val exprnProgrm: String,
    @SerializedName("exprnProgrmAt")
    val exprnProgrmAt: String,
    @SerializedName("extshrCo")
    val extshrCo: Int,
    @SerializedName("facltDivNm")
    val facltDivNm: String,
    @SerializedName("facltNm")
    val facltNm: String,
    @SerializedName("featureNm")
    val featureNm: String,
    @SerializedName("fireSensorCo")
    val fireSensorCo: Int,
    @SerializedName("firstImageUrl")
    val firstImageUrl: String,
    @SerializedName("frprvtSandCo")
    val frprvtSandCo: Int,
    @SerializedName("frprvtWrppCo")
    val frprvtWrppCo: Int,
    @SerializedName("glampInnerFclty")
    val glampInnerFclty: String,
    @SerializedName("glampSiteCo")
    val glampSiteCo: Int,
    @SerializedName("gnrlSiteCo")
    val gnrlSiteCo: Int,
    @SerializedName("homepage")
    val homepage: String,
    @SerializedName("induty")
    val induty: String,
    @SerializedName("indvdlCaravSiteCo")
    val indvdlCaravSiteCo: Int,
    @SerializedName("insrncAt")
    val insrncAt: String,
    @SerializedName("intro")
    val intro: String,
    @SerializedName("lctCl")
    val lctCl: String,
    @SerializedName("lineIntro")
    val lineIntro: String,
    @SerializedName("manageNmpr")
    val manageNmpr: Int,
    @SerializedName("manageSttus")
    val manageSttus: String,
    @SerializedName("mangeDivNm")
    val mangeDivNm: String,
    @SerializedName("mapX")
    val mapX: Double,
    @SerializedName("mapY")
    val mapY: Double,
    @SerializedName("modifiedtime")
    val modifiedtime: String,
    @SerializedName("operDeCl")
    val operDeCl: String,
    @SerializedName("operPdCl")
    val operPdCl: String,
    @SerializedName("posblFcltyCl")
    val posblFcltyCl: String,
    @SerializedName("prmisnDe")
    val prmisnDe: String,
    @SerializedName("resveCl")
    val resveCl: String,
    @SerializedName("resveUrl")
    val resveUrl: String,
    @SerializedName("sbrsCl")
    val sbrsCl: String,
    @SerializedName("sbrsEtc")
    val sbrsEtc: String,
    @SerializedName("sigunguNm")
    val sigunguNm: String,
    @SerializedName("siteBottomCl1")
    val siteBottomCl1: Int,
    @SerializedName("siteBottomCl2")
    val siteBottomCl2: Int,
    @SerializedName("siteBottomCl3")
    val siteBottomCl3: Int,
    @SerializedName("siteBottomCl4")
    val siteBottomCl4: Int,
    @SerializedName("siteBottomCl5")
    val siteBottomCl5: Int,
    @SerializedName("siteMg1Co")
    val siteMg1Co: Int,
    @SerializedName("siteMg1Vrticl")
    val siteMg1Vrticl: Int,
    @SerializedName("siteMg1Width")
    val siteMg1Width: Int,
    @SerializedName("siteMg2Co")
    val siteMg2Co: Int,
    @SerializedName("siteMg2Vrticl")
    val siteMg2Vrticl: Int,
    @SerializedName("siteMg2Width")
    val siteMg2Width: Int,
    @SerializedName("siteMg3Co")
    val siteMg3Co: Int,
    @SerializedName("siteMg3Vrticl")
    val siteMg3Vrticl: Int,
    @SerializedName("siteMg3Width")
    val siteMg3Width: Int,
    @SerializedName("sitedStnc")
    val sitedStnc: Int,
    @SerializedName("swrmCo")
    val swrmCo: Int,
    @SerializedName("tel")
    val tel: String,
    @SerializedName("themaEnvrnCl")
    val themaEnvrnCl: String,
    @SerializedName("toiletCo")
    val toiletCo: Int,
    @SerializedName("tourEraCl")
    val tourEraCl: String,
    @SerializedName("trlerAcmpnyAt")
    val trlerAcmpnyAt: String,
    @SerializedName("trsagntNo")
    val trsagntNo: String,
    @SerializedName("wtrplCo")
    val wtrplCo: Int,
    @SerializedName("zipcode")
    val zipcode: Int
) {

    fun toCampingItem(): CampingItem =
        CampingItem(
            contentId = contentId.toString(),
            name = facltNm,
            address = addr1,
            tel = tel,
            homepage = homepage,
            log = mapX.toString(),
            lat = mapY.toString()
        )
}