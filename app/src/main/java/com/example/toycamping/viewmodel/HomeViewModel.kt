package com.example.toycamping.viewmodel

import android.app.Application
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.repo.GoCampingRepository
import com.example.toycamping.ext.ioScope
import com.example.toycamping.room.entity.CampingEntity
import com.example.toycamping.utils.Result
import org.koin.java.KoinJavaComponent.inject

class HomeViewModel(app: Application) : BaseViewModel(app) {

    private val goCampingRepository by inject<GoCampingRepository>(GoCampingRepository::class.java)


    fun addBookmark(item: CampingEntity) {
        ioScope {
            if (goCampingRepository.isExistCampingEntity(item.name)) {

                when (val result =
                    goCampingRepository.toggleBookmarkCampingData(item)) {
                    is Result.Success -> {
                        viewStateChanged(HomeViewState.AddBookmark(result.data))
                    }

                    is Result.Error -> {
                        viewStateChanged(HomeViewState.Error("즐겨찾기 추가 실패."))
                    }
                }

            } else {
                if (goCampingRepository.registerCampingData(item)) {
                    viewStateChanged(HomeViewState.AddBookmark(item))
                } else {
                    viewStateChanged(HomeViewState.Error("즐겨찾기 추가 실패."))
                }
            }
        }
    }

    fun deleteBookmark(item: CampingEntity) {
        ioScope {
            when (val result =
                goCampingRepository.toggleBookmarkCampingData(item)) {
                is Result.Success -> {
                    viewStateChanged(HomeViewState.DeleteBookmark(result.data))
                }
                is Result.Error -> {
                    viewStateChanged(HomeViewState.Error("즐겨찾기 제거 실패."))
                }
            }
        }
    }

    fun permissionGrant() {
        viewStateChanged(HomeViewState.PermissionGrant)
    }


    sealed class HomeViewState : ViewState {
        data class Error(val errorMessage: String) : HomeViewState()
        data class AddBookmark(val item: CampingEntity) : HomeViewState()
        data class DeleteBookmark(val item: CampingEntity) : HomeViewState()
        object PermissionGrant : HomeViewState()
    }
}