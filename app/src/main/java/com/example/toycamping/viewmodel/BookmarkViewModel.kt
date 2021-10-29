package com.example.toycamping.viewmodel

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.model.CampingItem
import com.example.toycamping.data.model.toCampingItem
import com.example.toycamping.data.repo.FirebaseRepository
import com.example.toycamping.ext.ioScope
import org.koin.java.KoinJavaComponent.inject


class BookmarkViewModel(app: Application) : BaseViewModel(app), LifecycleObserver {

    private val firebaseRepository by inject<FirebaseRepository>(FirebaseRepository::class.java)

    fun getAllBookmark() {
        ioScope {
            firebaseRepository.getFirebaseAuth().currentUser?.email?.let { userId ->
                firebaseRepository.getFirebaseFireStore().collection(userId)
                    .document("camping")
                    .get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            if (it.result.exists()) {
                                val getResult: ArrayList<HashMap<String, String>> =
                                    it.result.get("like") as ArrayList<HashMap<String, String>>

                                val toResultList = getResult.map { it.toCampingItem() }

                                viewStateChanged(BookmarkViewState.BookmarkList(toResultList))

                            } else {
                                viewStateChanged(BookmarkViewState.EmptyBookmarkList)
                            }
                        } else {
                            viewStateChanged(BookmarkViewState.Error(it.exception?.message.toString()))
                        }
                    }
            }
        }
    }


    sealed class BookmarkViewState : ViewState {
        data class BookmarkList(val bookmarkList: List<CampingItem>) : BookmarkViewState()
        data class Error(val errorMessage: String) : BookmarkViewState()
        object EmptyBookmarkList : BookmarkViewState()
    }

}
