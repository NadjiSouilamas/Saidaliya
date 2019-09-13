package com.example.myapplication.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.Entity.Pharmacie
import com.example.myapplication.LocalStorage.RoomService
import com.example.myapplication.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ProximiteFragment : Fragment(), OnMapReadyCallback{

    lateinit var map : GoogleMap

    lateinit var pharmaList : List<Pharmacie>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.proximite_fragment, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        return view
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        pharmaList = RoomService.appDatabase.getPharmacieDao().getPharmaciesParVille("Alger")

        var firstPlace = LatLng(pharmaList.get(0).lat, pharmaList.get(0).lng)

        pharmaList.forEach {
            var place = LatLng(it.lat, it.lng)
            map.addMarker(MarkerOptions().position(place).title(it.nom))
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPlace, 13f))
    }
}
