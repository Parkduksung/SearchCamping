package com.example.toycamping.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.model.CampingItem
import com.example.toycamping.data.model.SnapItem
import com.example.toycamping.data.repo.FirebaseRepository
import com.example.toycamping.ext.ioScope
import com.google.firebase.firestore.FieldValue
import org.koin.java.KoinJavaComponent.inject

class HomeViewModel(app: Application) : BaseViewModel(app), LifecycleObserver {

    private val firebaseRepository by inject<FirebaseRepository>(FirebaseRepository::class.java)

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun firebaseStateObserver() {
        firebaseRepository.getFirebaseAuth().addAuthStateListener {
            checkLoginState(it.currentUser != null)
        }
    }

    fun addBookmarkItem(item: CampingItem) {
        ioScope {
            firebaseRepository.getFirebaseAuth().currentUser?.email?.let { userId ->

                firebaseRepository.addBookmarkItem(userId, item).addOnCompleteListener { dbResult ->
                    if (dbResult.isSuccessful) {
                        viewStateChanged(HomeViewState.AddBookmarkItem(item))
                    } else {
                        viewStateChanged(HomeViewState.Error("즐겨찾기 추가 실패."))
                    }
                }
            }
        }
    }

    fun deleteBookmarkItem(item: CampingItem) {
        ioScope {
            firebaseRepository.getFirebaseAuth().currentUser?.email?.let { userId ->

                firebaseRepository.deleteBookmarkItem(userId, item)
                    .addOnCompleteListener { dbResult ->
                        if (dbResult.isSuccessful) {
                            viewStateChanged(HomeViewState.DeleteBookmarkItem(item))
                        } else {
                            viewStateChanged(HomeViewState.Error("즐겨찾기 제거 실패."))
                        }
                    }
            }
        }
    }

    fun addSnapItem(item: SnapItem, uri: Uri) {
        ioScope {
            firebaseRepository.getFirebaseAuth().currentUser?.email?.let { userId ->
                firebaseRepository.addSnapItem(userId, uri, item)
                    .addOnCompleteListener { dbResult ->
                        if (dbResult.isSuccessful && dbResult.result != null) {

                            dbResult.result.storage.downloadUrl.addOnSuccessListener { downloadUri ->

                                val toDownloadUrlItem =
                                    item.copy(image = downloadUri.toString())

                                firebaseRepository.getFirebaseFireStore().collection(userId)
                                    .document("snap")
                                    .update("item", FieldValue.arrayUnion(toDownloadUrlItem))
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            viewStateChanged(
                                                HomeViewState.AddSnapItem(
                                                    toDownloadUrlItem
                                                )
                                            )
                                        } else {
                                            viewStateChanged(HomeViewState.Error("스냅 추가 실패."))
                                        }
                                    }
                            }
                        } else {
                            viewStateChanged(HomeViewState.Error("스냅 추가 실패."))
                        }
                    }
            }
        }
    }

    fun deleteSnapItem(item: SnapItem) {
        ioScope {
            firebaseRepository.getFirebaseAuth().currentUser?.email?.let { userId ->
                firebaseRepository.deleteSnapItem(userId, item)
                    .addOnCompleteListener { dbResult ->
                        if (dbResult.isSuccessful) {
                            viewStateChanged(HomeViewState.DeleteSnapItem(item))
                        } else {
                            viewStateChanged(HomeViewState.Error("즐겨찾기 제거 실패."))
                        }
                    }
            }
        }
    }

    fun permissionGrant() {
        viewStateChanged(HomeViewState.PermissionGrant)
    }

    private fun checkLoginState(isLogin: Boolean) {
        if (isLogin) {
            viewStateChanged(HomeViewState.LoginState)
        } else {
            viewStateChanged(HomeViewState.NotLoginState)
        }
    }

    sealed class HomeViewState : ViewState {
        data class Error(val errorMessage: String) : HomeViewState()
        data class AddBookmarkItem(val item: CampingItem) : HomeViewState()
        data class DeleteBookmarkItem(val item: CampingItem) : HomeViewState()
        data class AddSnapItem(val item: SnapItem) : HomeViewState()
        data class DeleteSnapItem(val item: SnapItem) : HomeViewState()
        object PermissionGrant : HomeViewState()
        object NotLoginState : HomeViewState()
        object LoginState : HomeViewState()
    }
}