package com.example.toycamping.viewmodel

import android.app.Application
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.model.SnapItem
import com.example.toycamping.data.model.toSnapItem
import com.example.toycamping.data.repo.FirebaseRepository
import com.example.toycamping.ext.ioScope
import org.koin.java.KoinJavaComponent

class SnapViewModel(app: Application) : BaseViewModel(app) {

    private val firebaseRepository by KoinJavaComponent.inject<FirebaseRepository>(
        FirebaseRepository::class.java
    )

    fun showAddSnapDialog() {
        viewStateChanged(SnapViewState.AddSnapDialog)
    }

    fun getAllSnapList() {

        viewStateChanged(SnapViewState.ShowProgress)
        firebaseRepository.getFirebaseAuth().currentUser?.email?.let { userId ->

            firebaseRepository.getFirebaseFireStore().collection(userId).document("snap").get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (it.result.exists()) {

                            val getResult: ArrayList<HashMap<String, String>>? =
                                it.result.get("item") as ArrayList<HashMap<String, String>>?

                            val toResultList = getResult?.map { it.toSnapItem() }

                            if (!toResultList.isNullOrEmpty()) {
                                viewStateChanged(
                                    SnapViewState.SnapList(
                                        toResultList
                                    )
                                )
                                viewStateChanged(SnapViewState.HideProgress)
                            } else {
                                viewStateChanged(SnapViewState.EmptySnapList)
                                viewStateChanged(SnapViewState.HideProgress)
                            }
                        } else {
                            viewStateChanged(SnapViewState.EmptySnapList)
                            viewStateChanged(SnapViewState.HideProgress)
                        }
                    } else {
                        viewStateChanged(BookmarkViewModel.BookmarkViewState.Error(it.exception?.message.toString()))
                        viewStateChanged(SnapViewState.HideProgress)
                    }
                }
        }
    }

    fun checkLoginState() {
        ioScope {
            if (firebaseRepository.getFirebaseAuth().currentUser == null) {
                viewStateChanged(SnapViewState.ShowLoginView)
            }
        }
    }


    sealed class SnapViewState : ViewState {
        data class Error(val message: String) : SnapViewState()
        data class SnapList(val list: List<SnapItem>) : SnapViewState()
        object EmptySnapList : SnapViewState()
        object AddSnapDialog : SnapViewState()
        object ShowLoginView : SnapViewState()
        object ShowProgress : SnapViewState()
        object HideProgress : SnapViewState()
    }
}