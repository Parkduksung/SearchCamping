package com.example.toycamping.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.toycamping.R
import com.example.toycamping.base.BaseFragment
import com.example.toycamping.base.ViewState
import com.example.toycamping.databinding.MapFragmentBinding
import com.example.toycamping.utils.GpsTracker
import com.example.toycamping.viewmodel.MapViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapFragment : BaseFragment<MapFragmentBinding>(R.layout.map_fragment) {


    private lateinit var gpsTracker: GpsTracker

    private val campingItemList = mutableSetOf<MapPOIItem>()

    private val mapViewModel by viewModel<MapViewModel>()

    private val mapViewEventListener =
        object : MapView.MapViewEventListener {
            override fun onMapViewInitialized(p0: MapView?) {

            }

            override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
                mapViewModel.currentCenterMapPoint.value = p1
            }

            override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {

            }

            override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {

            }

            override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {

            }

            override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {

            }

            override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {

            }

            override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {

            }

            override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {

            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        initViewModel()
    }

    private fun initUi() {

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

        with(binding) {
            containerMap.apply {
                setMapViewEventListener(this@MapFragment.mapViewEventListener)
                addPOIItem(mapPOIItem)
                setZoomLevel(8, false)
                setMapCenterPoint(currentMapPoint, false)
            }
        }
    }

    private fun initViewModel() {
        binding.viewModel = mapViewModel

        mapViewModel.viewStateLiveData.observe(requireActivity()) { viewState: ViewState? ->
            (viewState as? MapViewModel.MapViewState)?.let { onChangedViewState(viewState) }
        }

    }


    private fun onChangedViewState(viewState: ViewState) {
        when (viewState) {
            is MapViewModel.MapViewState.SetCurrentLocation -> {

                val currentMapPoint = MapPoint.mapPointWithGeoCoord(
                    gpsTracker.getCurrentLatitude(),
                    gpsTracker.getCurrentLongitude()
                )
                binding.containerMap.setMapCenterPoint(currentMapPoint, true)
            }

            is MapViewModel.MapViewState.GetGoCampingLocationList -> {
                GlobalScope.launch(Dispatchers.IO) {
                    viewState.itemList.forEach { item ->
                        val mapPOIItem = MapPOIItem().apply {
                            itemName = item.facltNm
                            mapPoint = MapPoint.mapPointWithGeoCoord(item.mapY, item.mapX)
                            markerType = MapPOIItem.MarkerType.RedPin
                        }
                        campingItemList.add(mapPOIItem)
                    }

                    withContext(Dispatchers.Main) {
                        binding.containerMap.addPOIItems(campingItemList.toTypedArray())
                    }
                }
            }

            is MapViewModel.MapViewState.Error -> {
                Toast.makeText(requireContext(), viewState.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getItemsAroundCurrent(mapPoint: MapPoint) {
        mapViewModel.getGoCampingLocationList(
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
                initUi()
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
            initUi()
        }

    }

}
