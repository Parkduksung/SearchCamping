package com.example.toycamping.viewmodel

import android.app.Application
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.model.NotificationItem
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
        ioScope {
            firebaseRepository.getFirebaseAuth().currentUser?.email?.let { userId ->

                firebaseRepository.getFirebaseFireStore().collection(userId).document("snap")
                    .delete().addOnCompleteListener {
                        if (it.isSuccessful) {

                            firebaseRepository.getFirebaseFireStore().collection(userId)
                                .document("camping").delete().addOnCompleteListener {

                                    if (it.isSuccessful) {

                                        firebaseRepository.getFirebaseFireStore().collection(userId)
                                            .document("nickname").delete().addOnCompleteListener {

                                                if (it.isSuccessful) {
                                                    firebaseRepository.getFirebaseAuth().currentUser?.delete()
                                                        ?.addOnCompleteListener {
                                                            if (it.isSuccessful) {
                                                                viewStateChanged(DashBoardViewState.WithdrawSuccess)
                                                            } else {
                                                                viewStateChanged(DashBoardViewState.WithdrawFailure)
                                                            }
                                                        }
                                                } else {
                                                    viewStateChanged(DashBoardViewState.WithdrawFailure)
                                                }
                                            }

                                    } else {
                                        viewStateChanged(DashBoardViewState.WithdrawFailure)
                                    }
                                }
                        } else {
                            viewStateChanged(DashBoardViewState.WithdrawFailure)
                        }
                    }
            }
        }
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
        ioScope {
            firebaseRepository.getFirebaseFireStore().collection("notification").get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (!it.result.isEmpty) {
                            val notifyList = mutableListOf<NotificationItem>()
                            for (dc in it.result.documents) {
                                val item = dc.toObject(NotificationItem::class.java)
                                item?.let { notificationItem -> notifyList.add(notificationItem) }
                            }
                            viewStateChanged(DashBoardViewState.ShowNotification(notifyList))
                        } else {
                            viewStateChanged(DashBoardViewState.EmptyNotificationItem)
                        }
                    } else {
                        viewStateChanged(DashBoardViewState.ErrorGetNotificationItem)
                    }
                }
        }
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

    fun getUserInfo() {
        firebaseRepository.getFirebaseAuth().currentUser?.email?.let { userId ->
            firebaseRepository.getFirebaseFireStore().collection(userId).document("nickname").get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        viewStateChanged(
                            DashBoardViewState.GetUserInfo(
                                userId,
                                it.result.get("nickname").toString()
                            )
                        )
                    }
                }
        }

    }

    fun routeDashboard() {
        viewStateChanged(DashBoardViewState.RouteDashboard)
    }


    sealed class DashBoardViewState : ViewState {
        data class GetUserInfo(val id: String, val nickname: String) : DashBoardViewState()
        object ShowLogoutDialog : DashBoardViewState()
        object ShowWithdrawDialog : DashBoardViewState()
        object ShowQuestion : DashBoardViewState()
        data class ShowNotification(val list: List<NotificationItem>) : DashBoardViewState()
        object ErrorGetNotificationItem : DashBoardViewState()
        object EmptyNotificationItem : DashBoardViewState()
        object ShowIdentify : DashBoardViewState()
        object LogoutSuccess : DashBoardViewState()
        object LogoutFailure : DashBoardViewState()
        object AddQuestionSuccess : DashBoardViewState()
        object AddQuestionFailure : DashBoardViewState()
        object WithdrawSuccess : DashBoardViewState()
        object WithdrawFailure : DashBoardViewState()
        object Error : DashBoardViewState()
        object RouteDashboard : DashBoardViewState()
    }

}