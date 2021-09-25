package com.example.toycamping.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.toycamping.databinding.MapFragmentBinding
import com.example.toycamping.utils.GpsTracker
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapFragment : Fragment() {

    private lateinit var binding: MapFragmentBinding

    private lateinit var mapView: MapView

    private lateinit var gpsTracker: GpsTracker

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
    }

    private fun loadMapView() {
        mapView = MapView(context)


        val searchMapPoint = MapPoint.mapPointWithGeoCoord(
            36.0345423,
            128.6142847,
        )

        val mapPOIItem = MapPOIItem().apply {
            itemName = "searchItem"
            mapPoint = searchMapPoint
        }

        mapView.addPOIItem(mapPOIItem)
        mapView.setMapCenterPoint(searchMapPoint, true)

        binding.containerMap.addView(mapView)
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
