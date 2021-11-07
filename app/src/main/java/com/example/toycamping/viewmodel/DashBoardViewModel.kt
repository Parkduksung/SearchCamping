package com.example.toycamping.viewmodel

import android.app.Application
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.model.QuestionItem
import com.example.toycamping.data.repo.FirebaseRepository
import com.example.toycamping.ext.ioScope
import org.koin.java.KoinJavaComponent.inject

class DashBoardViewModel(app: Application) : BaseViewModel(app) {

    private val firebaseRepository by inject<FirebaseRepository>(FirebaseRepository::class.java)

    private fun logout() {
        ioScope {
            if (firebaseRepository.logout()) {
                viewStateChanged(LoginViewModel.LoginViewState.LoginSuccess)
            } else {
                viewStateChanged(LoginViewModel.LoginViewState.LoginFailure)
            }
        }
    }

    private fun withdraw() {

    }

    fun checkType(type: String?) {
        if (type != null) {
            when (type) {
                "logout" -> {
                    logout()
                }

                "withdraw" -> {
                    withdraw()
                }
            }
        } else {
            viewStateChanged(DashBoardViewState.Error)
        }
    }


    fun showLogoutDialog() {
        viewStateChanged(DashBoardViewState.ShowLogoutDialog)
    }

    fun showWithdrawDialog() {
        viewStateChanged(DashBoardViewState.ShowWithdrawDialog)
    }

    fun showQuestion() {
        viewStateChanged(DashBoardViewState.ShowQuestion)
    }

    fun showNotification() {
        viewStateChanged(DashBoardViewState.ShowNotification)
    }

    fun showIdentify() {
        viewStateChanged(DashBoardViewState.ShowIdentify)
    }

    fun addQuestion(item: QuestionItem?) {
        item?.let {
            ioScope {
                firebaseRepository.getFirebaseFireStore().collection("question")
                    .document()
                    .set(item).addOnCompleteListener {
                        if (it.isSuccessful) {
                            viewStateChanged(DashBoardViewState.AddQuestionSuccess)
                        } else {
                            viewStateChanged(DashBoardViewState.AddQuestionFailure)
                        }
                    }
            }
        }
    }


    sealed class DashBoardViewState : ViewState {
        object ShowLogoutDialog : DashBoardViewState()
        object ShowWithdrawDialog : DashBoardViewState()
        object ShowQuestion : DashBoardViewState()
        object ShowNotification : DashBoardViewState()
        object ShowIdentify : DashBoardViewState()
        object LogoutSuccess : DashBoardViewState()
        object LogoutFailure : DashBoardViewState()
        object AddQuestionSuccess : DashBoardViewState()
        object AddQuestionFailure : DashBoardViewState()
        object WithdrawSuccess : DashBoardViewState()
        object WithdrawFailure : DashBoardViewState()
        object Error : DashBoardViewState()
    }

}