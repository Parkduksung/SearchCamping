package com.example.toycamping.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.example.toycamping.api.response.LocationItem
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.model.GoCampingItem
import com.example.toycamping.data.repo.GoCampingRepository
import org.koin.java.KoinJavaComponent.inject

class HomeViewModel(app: Application) : BaseViewModel(app) {

    private val goCampingRepository by inject<GoCampingRepository>(GoCampingRepository::class.java)

    @SuppressLint("LongLogTag")
    fun getGoCampingBasedList() {

        goCampingRepository.getBasedList(
            onSuccess = {

            }, onFailure = {

            }
        )
    }


    fun getGoCampingLocationList(longitude: Double, latitude: Double, radius: Int) {
        goCampingRepository.getLocationList(longitude, latitude, radius,
            onSuccess = {
                viewStateChanged(HomeViewState.GetGoCampingLocationList(it.response.body.items.item))
            }, onFailure = {
                Log.d("결과 error", it.message.toString())
            })

    }

    fun getSearchList(keyword: String) {

        goCampingRepository.getSearchList(keyword,
            onSuccess = {

            }, onFailure = {
            })

    }

    fun getImageList(contentId: String) {
        goCampingRepository.getImageList(contentId,
            onSuccess = {
                val urlList =
                    it.imageResponse.body.items.item.map { imageItem -> imageItem.imageUrl }
            }, onFailure = {
            })
    }

    sealed class HomeViewState : ViewState {
        data class GetGoCampingBasedList(val goCampingItem: GoCampingItem) : HomeViewState()
        data class GetGoCampingLocationList(val itemList: List<LocationItem>) : HomeViewState()
        object ErrorGetGoCampingBasedList : HomeViewState()
    }

}