package com.example.toycamping.ext

fun String.getDate(): String {
    val date = this.substring(0, lastIndex - 4)
    val year = date.substring(0, 4)
    val month = date.substring(4, 6)
    val day = date.substring(6, 8)
    return "$year.$month.$day"
}

fun String.convertDate() : String {
    val year = this.substring(0, 4)
    val month = this.substring(4, 6)
    val day = this.substring(6, 8)
    return "$year.$month.$day"
}