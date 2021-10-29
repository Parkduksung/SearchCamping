package com.example.toycamping.data.model

data class CampingItem(
    val contentId: String,
    val name: String,
    val address: String,
    val homepage: String? = null,
    val tel: String? = null,
    val lat: String,
    val log: String
)

fun HashMap<String, String>.toCampingItem(): CampingItem =
    CampingItem(
        contentId = getValue("contentId"),
        name = getValue("name"),
        address = getValue("address"),
        homepage = getValue("homepage"),
        tel = getValue("tel"),
        lat = getValue("lat"),
        log = getValue("log")
    )
