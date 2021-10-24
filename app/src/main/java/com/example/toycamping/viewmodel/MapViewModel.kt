package com.example.toycamping.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.example.toycamping.api.response.SearchItem
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.repo.GoCampingRepository
import com.example.toycamping.ext.ioScope
import com.example.toycamping.utils.GpsTracker
import com.example.toycamping.utils.Result
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.pow

class MapViewModel(app: Application) : BaseViewModel(app) {

    val currentCenterMapPoint = MutableLiveData<MapPoint>()

    val currentZoomLevel = MutableLiveData<Int>()

    val search = MutableLiveData<String>()

    private val goCampingRepository: GoCampingRepository by inject(GoCampingRepository::class.java)

    private val gpsTracker = GpsTracker(app)

    fun search() {
        search.value?.let {
            getSearchList(it)
        }
    }

    fun setCurrentLocation() {
        ioScope {

            viewStateChanged(MapViewState.ShowProgress)


            when (val result = gpsTracker.getLocation()) {
                is Result.Success -> {
                    result.data.addOnCompleteListener { task ->

                        val location = task.result

                        val resultMapPoint =
                            MapPoint.mapPointWithGeoCoord(location.latitude, location.longitude)

                        viewStateChanged(MapViewState.SetCurrentLocation(resultMapPoint))
                        viewStateChanged(MapViewState.HideProgress)

                    }
                }

                is Result.Error -> {
                    viewStateChanged(MapViewState.Error(result.exception.message.toString()))
                    viewStateChanged(MapViewState.HideProgress)
                }
            }
        }
    }

    private fun zoomLevelToRadius(zoomLevel: Int): Int {
        return if (zoomLevel == -2) {
            ((2.0).pow(-1) * 100).toInt()
        } else {
            ((2.0).pow(zoomLevel) * 100).toInt()
        }
    }


    fun searchCampingAroundCurrent() {
        currentCenterMapPoint.value?.let { mapPoint ->
            getGoCampingLocationList(
                mapPoint.mapPointGeoCoord.longitude,
                mapPoint.mapPointGeoCoord.latitude,
                zoomLevelToRadius(currentZoomLevel.value ?: 0)
            )
        }
    }

    fun getSearchList(keyword: String) {
        goCampingRepository.getSearchList(keyword,
            onSuccess = {
                viewStateChanged(MapViewState.GetSearchList(it.response.body.items.item))
            }, onFailure = {
                viewStateChanged(MapViewState.Error("캠핑장을 찾을 수 없습니다."))
            })
    }

    fun getGoCampingLocationList(longitude: Double, latitude: Double, radius: Int) {
        goCampingRepository.getLocationList(longitude, latitude, radius,
            onSuccess = {
                if (!it.response.body.items.item.isNullOrEmpty()) {

                    ioScope {
                        val campingItemList = mutableSetOf<MapPOIItem>()

                        val resultList = it.response.body.items.item.sortedBy {
                            getDistance(
                                currentCenterMapPoint.value!!,
                                MapPoint.mapPointWithGeoCoord(it.latitude, it.longitude)
                            )
                        }


                        val zoomLevel = autoZoomLevel(
                            getDistance(
                                currentCenterMapPoint.value!!,
                                MapPoint.mapPointWithGeoCoord(
                                    resultList[resultList.lastIndex].latitude,
                                    resultList[resultList.lastIndex].longitude
                                )
                            )
                        )

                        viewStateChanged(MapViewState.SetZoomLevel(zoomLevel))


                        resultList.forEach { item ->
                            val mapPOIItem = MapPOIItem().apply {
                                itemName = item.facltNm
                                mapPoint =
                                    MapPoint.mapPointWithGeoCoord(item.latitude, item.longitude)
                                markerType = MapPOIItem.MarkerType.RedPin
                            }
                            campingItemList.add(mapPOIItem)
                        }


                        viewStateChanged(MapViewState.GetGoCampingLocationList(campingItemList.toTypedArray()))

                    }
                } else {
                    viewStateChanged(MapViewState.Error("캠핑장을 찾을 수 없습니다."))
                }
            }, onFailure = {
                viewStateChanged(MapViewState.Error("캠핑장을 찾을 수 없습니다."))
            })
    }


    private fun autoZoomLevel(
        firstDistance: Int
    ): Int {

        var zoomLevel = 0

        for (i in -1..10) {
            if (i < 0) {
                if (((2.0).pow(i) * 100) > firstDistance && firstDistance >= 0) {
                    zoomLevel = i
                    break
                }
            } else if (i == 0) {
                if (((2.0).pow(i) * 100) > firstDistance && firstDistance >= ((2.0).pow(i - 1) * 100)) {
                    zoomLevel = i
                    break
                }
            } else if (i > 0) {
                if (((2.0).pow(i - 1) * 100) <= firstDistance && firstDistance < ((2.0).pow(i) * 100)) {
                    zoomLevel = i
                    break
                }
            }
        }
        return zoomLevel
    }

    private fun getDistance(oldCenter: MapPoint, currentCenter: MapPoint): Int {

        val locationA = Location("A")
        val locationB = Location("B")

        locationA.latitude = oldCenter.mapPointGeoCoord.latitude
        locationA.longitude = oldCenter.mapPointGeoCoord.longitude

        locationB.latitude = currentCenter.mapPointGeoCoord.latitude
        locationB.longitude = currentCenter.mapPointGeoCoord.longitude

        return locationA.distanceTo(locationB).toInt()
    }


    sealed class MapViewState : ViewState {
        data class SetZoomLevel(val zoomLevel: Int) : MapViewState()
        data class SetCurrentLocation(val mapPoint: MapPoint) : MapViewState()
        data class GetGoCampingLocationList(val itemList: Array<MapPOIItem>) : MapViewState()
        data class GetSearchList(val item: SearchItem) : MapViewState()
        data class Error(val errorMessage: String) : MapViewState()
        object ShowProgress : MapViewState()
        object HideProgress : MapViewState()
    }

}