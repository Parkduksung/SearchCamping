package com.example.toycamping.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.toycamping.databinding.MapFragmentBinding
import com.example.toycamping.utils.GpsTracker
import com.example.toycamping.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MapFragment : Fragment() {

    private lateinit var binding: MapFragmentBinding

    private lateinit var mapView: MapView

    private lateinit var gpsTracker: GpsTracker

    private val homeViewModel by sharedViewModel<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MapFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        initViewModel()
    }

    private fun initViewModel() {
        homeViewModel.homeViewStateLiveData.observe(requireActivity()) {
            onChangedHomeViewState(it)
        }
    }

    private val campingItemList = mutableSetOf<MapPOIItem>()

    @SuppressLint("LongLogTag")
    private fun onChangedHomeViewState(homeViewState: HomeViewModel.HomeViewState) {
        when (homeViewState) {
            is HomeViewModel.HomeViewState.GetGoCampingLocationList -> {
                GlobalScope.launch(Dispatchers.IO) {
                    homeViewState.itemList.forEach { item ->
                        Log.d("결과 - onChangedHomeViewState", item.facltNm)
                        val mapPOIItem = MapPOIItem().apply {
                            itemName = item.facltNm
                            mapPoint = MapPoint.mapPointWithGeoCoord(item.mapY, item.mapX)
                            markerType = MapPOIItem.MarkerType.RedPin
                        }
                        campingItemList.add(mapPOIItem)
                    }

                    withContext(Dispatchers.Main) {
                        mapView.addPOIItems(campingItemList.toTypedArray())
                    }
                }
            }
        }
    }


    private fun loadMapView() {
        mapView = MapView(context)

        binding.containerMap.addView(mapView)


        gpsTracker = GpsTracker(requireActivity())

        val currentMapPoint = MapPoint.mapPointWithGeoCoord(
            gpsTracker.getCurrentLatitude(),
            gpsTracker.getCurrentLongitude()
        )

        getItemsAroundCurrent(currentMapPoint)


        val mapPOIItem = MapPOIItem().apply {
            itemName = "searchItem"
            mapPoint = currentMapPoint
        }

        mapView.addPOIItem(mapPOIItem)
        mapView.setZoomLevel(8, true)
        mapView.setMapCenterPoint(currentMapPoint, true)


    }


    private fun getItemsAroundCurrent(mapPoint: MapPoint) {
        homeViewModel.getGoCampingLocationList(
            latitude = mapPoint.mapPointGeoCoord.latitude,
            longitude = mapPoint.mapPointGeoCoord.longitude,
            radius = 20000
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            999 -> {
                loadMapView()
            }

            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }

    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 999
            )
        } else {
            loadMapView()
        }

    }

}
