package com.example.toycamping.viewmodel

import android.app.Application
import com.example.toycamping.api.response.LocationItem
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.repo.GoCampingRepository
import org.koin.java.KoinJavaComponent.inject

class MapViewModel(app: Application) : BaseViewModel(app) {

    private val goCampingRepository: GoCampingRepository by inject(GoCampingRepository::class.java)


    fun setCurrentLocation() {
        viewStateChanged(MapViewState.SetCurrentLocation)
    }

    fun getGoCampingLocationList(longitude: Double, latitude: Double, radius: Int) {
        goCampingRepository.getLocationList(longitude, latitude, radius,
            onSuccess = {
                viewStateChanged(MapViewState.GetGoCampingLocationList(it.response.body.items.item))
            }, onFailure = {
                viewStateChanged(MapViewState.Error(it.message.toString()))
            })
    }

    sealed class MapViewState : ViewState {
        object SetCurrentLocation : MapViewState()
        data class GetGoCampingLocationList(val itemList: List<LocationItem>) : MapViewState()
        data class Error(val errorMessage: String) : MapViewState()
    }


}