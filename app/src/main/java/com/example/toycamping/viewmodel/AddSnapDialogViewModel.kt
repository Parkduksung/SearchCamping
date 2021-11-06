package com.example.toycamping.viewmodel

import android.app.Application
import android.net.Uri
import androidx.databinding.ObservableField
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.model.SnapItem
import java.text.SimpleDateFormat
import java.util.*

class AddSnapDialogViewModel(app: Application) : BaseViewModel(app) {

    val titleObservableField = ObservableField<String>()
    val detailObservableField = ObservableField<String>()
    val imageUriObservableField = ObservableField<Uri>()

    fun addSnap() {
        checkAddSnap()?.let {
            viewStateChanged(AddSnapDialogViewState.AddSnap(it, imageUriObservableField.get()!!))
        }
    }

    private fun checkAddSnap(): SnapItem? {
        return when {
            titleObservableField.get().isNullOrEmpty() -> {
                viewStateChanged(AddSnapDialogViewState.Error("제목을 입력하세요."))
                null
            }
            detailObservableField.get().isNullOrEmpty() -> {
                viewStateChanged(AddSnapDialogViewState.Error("내용을 입력하세요."))
                null
            }
            imageUriObservableField.get() == null -> {
                viewStateChanged(AddSnapDialogViewState.Error("대표사진을 추가하세요."))
                null
            }
            else -> {
                val imageFileName = SimpleDateFormat("yyyyMMddHHmmss").format(Date()) + ".png"
                SnapItem(
                    name = titleObservableField.get(),
                    detail = detailObservableField.get(),
                    date = imageFileName
                )
            }
        }
    }


    sealed class AddSnapDialogViewState : ViewState {
        data class AddSnap(val item: SnapItem, val uri: Uri) : AddSnapDialogViewState()
        data class Error(val message: String) : AddSnapDialogViewState()
    }
}