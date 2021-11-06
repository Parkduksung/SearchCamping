package com.example.toycamping.viewmodel

import android.app.Application
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.model.SnapItem
import com.example.toycamping.data.model.toSnapItem
import com.example.toycamping.data.repo.FirebaseRepository
import org.koin.java.KoinJavaComponent

class SnapViewModel(app: Application) : BaseViewModel(app) {

    private val firebaseRepository by KoinJavaComponent.inject<FirebaseRepository>(
        FirebaseRepository::class.java
    )

    fun showAddSnapDialog() {
        viewStateChanged(SnapViewState.AddSnapDialog)
    }

    fun getAllSnapList() {

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
                            } else {
                                viewStateChanged(SnapViewState.EmptySnapList)
                            }
                        } else {
                            viewStateChanged(SnapViewState.EmptySnapList)
                        }
                    } else {
                        viewStateChanged(BookmarkViewModel.BookmarkViewState.Error(it.exception?.message.toString()))
                    }
                }
        }
    }


    sealed class SnapViewState : ViewState {
        data class Error(val message: String) : SnapViewState()
        data class SnapList(val list: List<SnapItem>) : SnapViewState()
        object EmptySnapList : SnapViewState()
        object AddSnapDialog : SnapViewState()
    }
}