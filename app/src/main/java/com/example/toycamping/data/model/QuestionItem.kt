package com.example.toycamping.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionItem(
    val date: String? = null,
    val detail: String? = null
) : Parcelable