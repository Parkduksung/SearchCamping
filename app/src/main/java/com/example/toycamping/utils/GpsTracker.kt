package com.example.toycamping.utils

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat

class GpsTracker(private val context: Context) : LocationListener {

    init {
        getLocation()
    }

    //위치를 표현할때 사용하는 클래스.
    private lateinit var location: Location

    //현재의 경도
    private var currentLongitude: Double = 0.0

    //현재의 위도
    private var currentLatitude: Double = 0.0


    //외부에서 현재 위도, 경도 (캡슐화 원리)
    fun getCurrentLongitude(): Double = currentLongitude
    fun getCurrentLatitude(): Double = currentLatitude


    //현재 위도 경도 구하는 로직.
    private fun getLocation(): Location? {

        try {
            val locationManager =
                context.getSystemService(LOCATION_SERVICE) as LocationManager

            val isGPSEnabled =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("getLocationError", "!isGPSEnabled && !isNetworkEnabled")
                return null
            } else {


                val hasFineLocationPermission =
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )

                val hasCoarseLocationPermission =
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )

                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                    hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
                ) {

                    if (isNetworkEnabled) {
                        try {
                            locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                this
                            )

                            location =
                                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!!

                            currentLatitude = location.latitude
                            currentLongitude = location.longitude

                        } catch (e: Exception) {
                            Log.d("getLocationError", e.toString())
                            return null
                        }
                    }

                    if (isGPSEnabled) {
                        try {
                            locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                this
                            )

                            location =
                                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!

                            currentLatitude = location.latitude
                            currentLongitude = location.longitude

                        } catch (e: Exception) {
                            Log.d("getLocationError", e.toString())
                            return null
                        }
                    }


                } else {
                    Log.d("getLocationError", "permission denied")
                    return null
                }
            }
        } catch (e: Exception) {
            Log.d("getLocationError", e.toString())
            return null
        }

        return location
    }


    override fun onLocationChanged(location: Location) {

    }


    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f
        private const val MIN_TIME_BW_UPDATES: Long = 1000 * 60 * 1L
    }
}