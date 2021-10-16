package com.example.toycamping.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.toycamping.api.response.LocationItem
import com.example.toycamping.api.response.SearchItem
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.repo.GoCampingRepository
import com.example.toycamping.utils.GpsTracker
import com.example.toycamping.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.daum.mf.map.api.MapPoint
import org.koin.java.KoinJavaComponent.inject

class MapViewModel(app: Application) : BaseViewModel(app) {

    val currentCenterMapPoint = MutableLiveData<MapPoint>()

    val search = MutableLiveData<String>()

    private val goCampingRepository: GoCampingRepository by inject(GoCampingRepository::class.java)

    private val gpsTracker = GpsTracker(app)

    fun search() {
        search.value?.let {
            getSearchList(it)
        }
    }

    fun setCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = gpsTracker.getLocation()) {
                is Result.Success -> {
                    result.data.addOnCompleteListener { task ->

                        val location = task.result

                        val resultMapPoint =
                            MapPoint.mapPointWithGeoCoord(location.latitude, location.longitude)

                        viewStateChanged(MapViewState.SetCurrentLocation(resultMapPoint))
                    }
                }

                is Result.Error -> {
                    withContext(Dispatchers.Main) {
                        viewStateChanged(MapViewState.Error(result.exception.message.toString()))
                    }
                }
            }
        }
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

    fun getSearchList(keyword: String) {
        goCampingRepository.getSearchList(keyword,
            onSuccess = {
                viewStateChanged(MapViewState.GetSearchList(it.response.body.items.item[0]))
            }, onFailure = {
                viewStateChanged(MapViewState.Error("캠핑장을 찾을 수 없습니다."))
            })
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
        data class SetCurrentLocation(val mapPoint: MapPoint) : MapViewState()
        data class GetGoCampingLocationList(val itemList: List<LocationItem>) : MapViewState()
        data class GetSearchList(val item: SearchItem) : MapViewState()
        data class Error(val errorMessage: String) : MapViewState()
    }

}