package com.team.project

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.team.project.auth.LoginActivity
import android.provider.Settings

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var longitude = 0.0
    private var latitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        checkLocationPermission()

        auth = Firebase.auth

        if(auth.currentUser?.uid == null) {
            Log.d("SplashActivity", "null")

            Handler().postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 4000)

        } else {
            Log.d("SplashActivity", "not null")

            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 4000)
        }

    }

    override fun onStart() {
        super.onStart()
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        val accessLocation =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (accessLocation == PackageManager.PERMISSION_GRANTED) {
            checkLocationSetting()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                GPS_UTIL_LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GPS_UTIL_LOCATION_PERMISSION_REQUEST_CODE) {
            for (i in permissions.indices) {
                if (Manifest.permission.ACCESS_FINE_LOCATION == permissions[i]) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        checkLocationSetting()
                    } else {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("위치 권한이 꺼져있습니다.")
                        builder.setMessage("[권한] 설정에서 위치 권한을 허용해야 합니다.")
                        builder.setPositiveButton("설정으로 가기") { dialog, which ->
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                            .setNegativeButton("종료") { dialog, which -> finish() }
                        val alert = builder.create()
                        alert.show()
                    }
                    break
                }
            }
        }
    }

    private fun checkLocationSetting() {
        locationRequest = LocationRequest.create()
        locationRequest!!.setPriority(DEFAULT_LOCATION_REQUEST_PRIORITY)
        locationRequest!!.setInterval(DEFAULT_LOCATION_REQUEST_INTERVAL)
        locationRequest!!.setFastestInterval(DEFAULT_LOCATION_REQUEST_FAST_INTERVAL)
        val settingsClient = LocationServices.getSettingsClient(this)
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            .setAlwaysShow(true)
        settingsClient.checkLocationSettings(builder.build())
            .addOnSuccessListener(this) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@SplashActivity)

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                }
                fusedLocationProviderClient!!.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    null
                )
            }
            .addOnFailureListener(this@SplashActivity, OnFailureListener { e ->
                val statusCode = (e as ApiException).statusCode
                when (statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val rae = e as ResolvableApiException
                        rae.startResolutionForResult(
                            this@SplashActivity,
                            GPS_UTIL_LOCATION_RESOLUTION_REQUEST_CODE
                        )
                    } catch (sie: IntentSender.SendIntentException) {
                        Log.w(
                            TAG,
                            "unable to start resolution for result due to " + sie.localizedMessage
                        )
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage =
                            "location settings are inadequate, and cannot be fixed here. Fix in Settings."
                        Log.e(TAG, errorMessage)
                    }
                }
            })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GPS_UTIL_LOCATION_RESOLUTION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                checkLocationSetting()
            } else {
                finish()
            }
        }
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            longitude = locationResult.lastLocation.longitude
            latitude = locationResult.lastLocation.latitude
            fusedLocationProviderClient!!.removeLocationUpdates(this)


//            if(auth.currentUser?.uid != null) {
//                Log.d("SplashActivity", "null")
//
//                val intent = Intent(this@SplashActivity, MainActivity::class.java)
//                intent.putExtra("latitude", latitude)
//                intent.putExtra("longitude", longitude)
//                startActivity(intent)
//                finish()
//
//            } else {
//                Log.d("SplashActivity", "not null")
//
//                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
//                intent.putExtra("latitude", latitude)
//                intent.putExtra("longitude", longitude)
//                startActivity(intent)
//                finish()
//
//            }

        }

        override fun onLocationAvailability(locationAvailability: LocationAvailability) {
            super.onLocationAvailability(locationAvailability)
            Log.i(TAG, "onLocationAvailability - $locationAvailability")
        }
    }

    companion object {
        private val TAG = SplashActivity::class.java.simpleName
        const val GPS_UTIL_LOCATION_PERMISSION_REQUEST_CODE = 100
        private const val GPS_UTIL_LOCATION_RESOLUTION_REQUEST_CODE = 101
        const val DEFAULT_LOCATION_REQUEST_PRIORITY =
            LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        const val DEFAULT_LOCATION_REQUEST_INTERVAL = 20000L
        const val DEFAULT_LOCATION_REQUEST_FAST_INTERVAL = 10000L
    }
}