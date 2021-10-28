package com.example.toycamping.viewmodel

import android.app.Application
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.repo.FirebaseRepository
import com.example.toycamping.ext.ioScope
import org.koin.java.KoinJavaComponent.inject

class DashBoardViewModel(app: Application) : BaseViewModel(app) {

    private val firebaseRepository by inject<FirebaseRepository>(FirebaseRepository::class.java)

    fun logout() {
        ioScope {
            if (firebaseRepository.logout()) {
                viewStateChanged(LoginViewModel.LoginViewState.LoginSuccess)
            } else {
                viewStateChanged(LoginViewModel.LoginViewState.LoginFailure)
            }
        }
    }

    sealed class DashBoardViewState : ViewState {
        object LogoutSuccess : DashBoardViewState()
        object LogoutFailure : DashBoardViewState()
    }


}