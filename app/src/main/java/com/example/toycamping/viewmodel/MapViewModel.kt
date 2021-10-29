package com.example.toycamping.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.example.toycamping.api.response.SearchItem
import com.example.toycamping.base.BaseViewModel
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.model.CampingItem
import com.example.toycamping.data.model.toCampingItem
import com.example.toycamping.data.repo.FirebaseRepository
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

    private val firebaseRepository by inject<FirebaseRepository>(FirebaseRepository::class.java)

    private val gpsTracker = GpsTracker(app)

    fun search() {
        search.value?.let {
            getSearchList(it)
        }
    }

    fun checkBookmarkState(itemName: String) {
        goCampingRepository.getSearchList(itemName,
            onSuccess = { searchItem ->

                val toCampingItem = searchItem.response.body.items.item.toCampingItem()

                firebaseRepository.getFirebaseAuth().currentUser?.email?.let { userId ->
                    firebaseRepository.getFirebaseFireStore().collection(userId)
                        .document("camping")
                        .get().addOnCompleteListener {
                            if (it.isSuccessful) {
                                if (it.result.exists()) {
                                    val getResult: ArrayList<HashMap<String, String>> =
                                        it.result.get("like") as ArrayList<HashMap<String, String>>

                                    val toResultList = getResult.map { it.toCampingItem() }

                                    viewStateChanged(
                                        MapViewState.BookmarkState(
                                            toResultList.contains(
                                                toCampingItem
                                            )
                                        )
                                    )
                                } else {
                                    viewStateChanged(MapViewState.BookmarkState(false))
                                }
                            } else {
                                viewStateChanged(MapViewState.BookmarkState(false))
                            }
                        }
                }
            }, onFailure = {
                viewStateChanged(MapViewState.BookmarkState(false))
            }
        )
    }

    fun toggleBookmarkItem(itemName: String, isBookmark: Boolean) {
        goCampingRepository.getSearchList(itemName,
            onSuccess = {
                val toCampingItem = it.response.body.items.item.toCampingItem()
                if (isBookmark) {
                    viewStateChanged(MapViewState.AddBookmarkItem(toCampingItem))
                } else {
                    viewStateChanged(MapViewState.DeleteBookmarkItem(toCampingItem))
                }
            }, onFailure = {

            }
        )

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

    fun getSelectPOIItemInfo(itemName: String) {
        viewStateChanged(MapViewState.ShowProgress)
        goCampingRepository.getSearchList(itemName,
            onSuccess = {
                viewStateChanged(MapViewState.GetSelectPOIItem(it.response.body.items.item))
                viewStateChanged(MapViewState.HideProgress)
            }, onFailure = {
                viewStateChanged(MapViewState.Error("캠핑장 정보를 찾을 수 없습니다."))
                viewStateChanged(MapViewState.HideProgress)
            })
    }


    private fun getSearchList(keyword: String) {
        goCampingRepository.getSearchList(keyword,
            onSuccess = {
                ioScope {
                    val mapPOIItem = MapPOIItem().apply {
                        itemName = it.response.body.items.item.facltNm
                        mapPoint =
                            MapPoint.mapPointWithGeoCoord(
                                it.response.body.items.item.mapY,
                                it.response.body.items.item.mapX
                            )
                        markerType = MapPOIItem.MarkerType.RedPin
                    }

                    viewStateChanged(MapViewState.GetSearchList(mapPOIItem))
                }

            }, onFailure = {
                viewStateChanged(MapViewState.Error("캠핑장을 찾을 수 없습니다."))
            })
    }

    private fun getGoCampingLocationList(longitude: Double, latitude: Double, radius: Int) {
        viewStateChanged(MapViewState.ShowProgress)
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
                        viewStateChanged(MapViewState.HideProgress)
                    }
                } else {
                    viewStateChanged(MapViewState.Error("캠핑장을 찾을 수 없습니다."))
                    viewStateChanged(MapViewState.HideProgress)
                }
            }, onFailure = {
                viewStateChanged(MapViewState.Error("캠핑장을 찾을 수 없습니다."))
                viewStateChanged(MapViewState.HideProgress)
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
        data class GetSelectPOIItem(val item: SearchItem) : MapViewState()
        data class GetSearchList(val item: MapPOIItem) : MapViewState()
        data class Error(val errorMessage: String) : MapViewState()
        object ShowProgress : MapViewState()
        object HideProgress : MapViewState()
        data class BookmarkState(val isChecked: Boolean) : MapViewState()
        data class AddBookmarkItem(val item: CampingItem) : MapViewState()
        data class DeleteBookmarkItem(val item: CampingItem) : MapViewState()
    }

}