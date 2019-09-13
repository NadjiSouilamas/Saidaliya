package com.example.myapplication.Fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.Entity.Pharmacie
import com.example.myapplication.LocalStorage.RoomService
import com.example.myapplication.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ProximiteFragment : Fragment(), OnMapReadyCallback {

    lateinit var map: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val MY_PERMISSIONS_REQUEST_LOCATION = 1

    lateinit var pharmaList: List<Pharmacie>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.proximite_fragment, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.activity!!)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        return view
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        displayContent()

    }

    fun displayContent() {

        if (ContextCompat.checkSelfPermission(
                this.activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this.activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Toast.makeText(
                    this.activity!!,
                    "On doit connaitre votre localisation pour pouvoir vous aidez",
                    Toast.LENGTH_SHORT
                ).show()
                return
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this.activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener {

                var lat = it!!.latitude
                var lng = it!!.longitude

                pharmaList = RoomService.appDatabase.getPharmacieDao().getAllPharmacies()
                pharmaList.forEach {
                    // Checking it it's near my location
                    if (it.lat < lat + 0.1 && it.lat > lat - 0.1
                        && it.lng < lng + 0.1 && it.lng > lng - 0.1
                    ) {
                        var place = LatLng(it.lat, it.lng)
                        map.addMarker(MarkerOptions().position(place).title(it.nom))
                    }
                }
                map.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title("Ma Position"))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 13f))

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    )
    {
        if (ContextCompat.checkSelfPermission(
                    this.activity!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                Log.d("TAGTAG", "here")
                fusedLocationClient.lastLocation.addOnSuccessListener {

                    var lat = it!!.latitude
                    var lng = it!!.longitude

                    Log.d("TAGTAG", it!!.latitude.toString())
                    pharmaList = RoomService.appDatabase.getPharmacieDao().getAllPharmacies()
                    pharmaList.forEach {
                        // Checking it it's near my location
                        if (it.lat < lat + 0.1 && it.lat > lat - 0.1
                            && it.lng < lng + 0.1 && it.lng > lng - 0.1
                        ) {
                            var place = LatLng(it.lat, it.lng)
                            map.addMarker(MarkerOptions().position(place).title(it.nom))
                        }
                    }
                    map.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title("Ma Position"))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 13f))
                }
            }
        }
}
