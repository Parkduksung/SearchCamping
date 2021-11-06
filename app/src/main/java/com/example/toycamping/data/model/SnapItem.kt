package com.example.toycamping.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SnapItem(
    var name: String? = null,
    var date: String? = null,
    var image: String? = null,
    var detail: String? = null
) : Parcelable

fun HashMap<String, String>.toSnapItem(): SnapItem =
    SnapItem(
        name = getValue("name"),
        date = getValue("date"),
        image = getValue("image"),
        detail = getValue("detail")
    )