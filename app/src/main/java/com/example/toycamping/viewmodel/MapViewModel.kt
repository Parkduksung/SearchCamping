package com.example.toycamping.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.toycamping.api.response.LocationItem
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.repo.GoCampingRepository
import net.daum.mf.map.api.MapPoint
import org.koin.java.KoinJavaComponent.inject

class MapViewModel(app: Application) : BaseViewModel(app) {

    val currentCenterMapPoint = MutableLiveData<MapPoint>()

    private val goCampingRepository: GoCampingRepository by inject(GoCampingRepository::class.java)


    fun setCurrentLocation() {
        viewStateChanged(MapViewState.SetCurrentLocation)
    }

    fun searchCampingAroundCurrent() {
        currentCenterMapPoint.value?.let { mapPoint ->
            getGoCampingLocationList(
                mapPoint.mapPointGeoCoord.longitude,
                mapPoint.mapPointGeoCoord.latitude,
                20000
            )
        }
    }

    fun getGoCampingLocationList(longitude: Double, latitude: Double, radius: Int) {
        goCampingRepository.getLocationList(longitude, latitude, radius,
            onSuccess = {
                if (!it.response.body.items.item.isNullOrEmpty()) {
                    viewStateChanged(MapViewState.GetGoCampingLocationList(it.response.body.items.item))
                } else {
                    viewStateChanged(MapViewState.Error("캠핑장을 찾을 수 없습니다."))
                }
            }, onFailure = {
                viewStateChanged(MapViewState.Error("캠핑장을 찾을 수 없습니다."))
            })
    }

    sealed class MapViewState : ViewState {
        object SetCurrentLocation : MapViewState()
        data class GetGoCampingLocationList(val itemList: List<LocationItem>) : MapViewState()
        data class Error(val errorMessage: String) : MapViewState()
    }

}