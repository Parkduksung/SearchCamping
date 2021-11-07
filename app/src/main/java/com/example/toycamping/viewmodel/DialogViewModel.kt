package com.example.toycamping.viewmodel

import android.app.Application
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState

class DialogViewModel(app: Application) : BaseViewModel(app) {


    fun clickOK() {
        viewStateChanged(MyPageDialogViewState.ClickOK)
    }

    fun clickNO() {
        viewStateChanged(MyPageDialogViewState.ClickNO)
    }


    sealed class MyPageDialogViewState : ViewState {
        object ClickOK : MyPageDialogViewState()
        object ClickNO : MyPageDialogViewState()
        data class Error(val message: String) : MyPageDialogViewState()
    }

}