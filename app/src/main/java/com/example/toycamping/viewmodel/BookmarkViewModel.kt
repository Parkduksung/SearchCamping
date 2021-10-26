package com.example.toycamping.viewmodel

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.repo.GoCampingRepository
import com.example.toycamping.ext.ioScope
import com.example.toycamping.room.entity.CampingEntity
import org.koin.java.KoinJavaComponent
import com.example.toycamping.utils.Result

class BookmarkViewModel(app: Application) : BaseViewModel(app), LifecycleObserver {

    private val goCampingRepository: GoCampingRepository by KoinJavaComponent.inject(
        GoCampingRepository::class.java
    )


    fun getAllBookmark() {
        ioScope {

            when (val result = goCampingRepository.getAllBookmarkList()) {

                is Result.Success -> {

                    if (result.data.isNotEmpty()) {
                        viewStateChanged(BookmarkViewState.BookmarkList(result.data))
                    } else {
                        viewStateChanged(BookmarkViewState.EmptyBookmarkList)
                    }
                }

                is Result.Error -> {
                    viewStateChanged(BookmarkViewState.Error(result.exception.message.toString()))
                }
            }
        }
    }


    sealed class BookmarkViewState : ViewState {
        data class BookmarkList(val bookmarkList: List<CampingEntity>) : BookmarkViewState()
        data class Error(val errorMessage: String) : BookmarkViewState()
        object EmptyBookmarkList : BookmarkViewState()
    }

}
