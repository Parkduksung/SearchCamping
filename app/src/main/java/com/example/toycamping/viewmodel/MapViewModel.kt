package com.example.toycamping.viewmodel

import android.app.Application
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

    fun checkLoginState() {
        ioScope {
            if (firebaseRepository.getFirebaseAuth().currentUser == null) {
                viewStateChanged(MapViewState.ShowLoginView)
            }
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
                                    val getResult: ArrayList<HashMap<String, String>>? =
                                        it.result.get("like") as ArrayList<HashMap<String, String>>?

                                    val toResultList = getResult?.map { it.toCampingItem() }

                                    if (!toResultList.isNullOrEmpty()) {
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
                zoomLevelToRadius(50000)
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

                        val resultList = it.response.body.items.item

                        viewStateChanged(MapViewState.SetZoomLevel(8))

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

    fun routeSearch() {
        viewStateChanged(MapViewState.RouteSearch)
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
        object ShowLoginView : MapViewState()
        object RouteSearch : MapViewState()
        data class BookmarkState(val isChecked: Boolean) : MapViewState()
        data class AddBookmarkItem(val item: CampingItem) : MapViewState()
        data class DeleteBookmarkItem(val item: CampingItem) : MapViewState()
    }

}