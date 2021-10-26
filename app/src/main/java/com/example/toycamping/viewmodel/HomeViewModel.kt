package com.example.toycamping.viewmodel

import android.app.Application
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.repo.GoCampingRepository
import com.example.toycamping.ext.ioScope
import com.example.toycamping.utils.Result
import org.koin.java.KoinJavaComponent.inject

class HomeViewModel(app: Application) : BaseViewModel(app) {

    private val goCampingRepository by inject<GoCampingRepository>(GoCampingRepository::class.java)


    fun addBookmark(itemName: String) {

        ioScope {
            if (goCampingRepository.isExistCampingEntity(itemName)) {
                when (val result = goCampingRepository.getCampingData(itemName)) {

                    is Result.Success -> {
                        when (val result =
                            goCampingRepository.toggleBookmarkCampingData(result.data)) {
                            is Result.Success -> {
                                viewStateChanged(HomeViewState.AddBookmark(itemName))
                            }

                            is Result.Error -> {
                                viewStateChanged(HomeViewState.Error("즐겨찾기 추가 실패."))
                            }
                        }
                    }
                    is Result.Error -> {
                        viewStateChanged(HomeViewState.Error("즐겨찾기 추가 실패."))
                    }
                }

            } else {
                goCampingRepository.getSearchList(itemName,
                    onSuccess = {
                        ioScope {
                            if (goCampingRepository.registerCampingData(
                                    it.response.body.items.item.toCampingEntity(
                                        true
                                    )
                                )
                            ) {
                                viewStateChanged(HomeViewState.AddBookmark(itemName))
                            } else {
                                viewStateChanged(HomeViewState.Error("즐겨찾기 추가 실패."))
                            }
                        }
                    }, onFailure = {
                        viewStateChanged(HomeViewState.Error("즐겨찾기 추가 실패."))
                    }
                )
            }
        }
    }

    fun deleteBookmark(itemName: String) {
        ioScope {
            when (val result = goCampingRepository.getCampingData(itemName)) {

                is Result.Success -> {
                    when (val result =
                        goCampingRepository.toggleBookmarkCampingData(result.data)) {
                        is Result.Success -> {
                            viewStateChanged(HomeViewState.DeleteBookmark(itemName))
                        }
                        is Result.Error -> {
                            viewStateChanged(HomeViewState.Error("즐겨찾기 제거 실패."))
                        }
                    }
                }
                is Result.Error -> {
                    viewStateChanged(HomeViewState.Error("즐겨찾기 제거 실패."))
                }
            }
        }
    }


    sealed class HomeViewState : ViewState {
        data class Error(val errorMessage: String) : HomeViewState()
        data class AddBookmark(val itemName: String) : HomeViewState()
        data class DeleteBookmark(val itemName: String) : HomeViewState()
    }

    companion object {


    }

}