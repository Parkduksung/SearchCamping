package com.example.toycamping.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.toycamping.data.model.GoCampingItem
import com.example.toycamping.data.repo.GoCampingRepository
import org.koin.java.KoinJavaComponent.inject

class HomeViewModel : ViewModel() {

    private val goCampingRepository by inject<GoCampingRepository>(GoCampingRepository::class.java)

    private val _homeViewStateLiveData = MutableLiveData<HomeViewState>()
    val homeViewStateLiveData: LiveData<HomeViewState> = _homeViewStateLiveData


    @SuppressLint("LongLogTag")
    fun getGoCampingBasedList() {

        goCampingRepository.getBasedList(
            onSuccess = {

            }, onFailure = {

            }
        )
    }


    fun getGoCampingLocationList(mapX: Double, mapY: Double, radius: Int) {
        goCampingRepository.getLocationList(mapX, mapY, radius,
            onSuccess = {

            }, onFailure = {

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

    sealed class HomeViewState {
        data class GetGoCampingBasedList(val goCampingItem: GoCampingItem) : HomeViewState()
        object ErrorGetGoCampingBasedList : HomeViewState()
    }

}