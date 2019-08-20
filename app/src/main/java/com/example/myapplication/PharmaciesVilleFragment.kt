package com.example.myapplication

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapters.PharmaciesAdapter
import com.example.myapplication.Entity.Pharmacie
import com.example.myapplication.LocalStorage.RoomService
import kotlinx.android.synthetic.main.pharmacies_ville_fragment.*

class Pharmacie_ville_fragment : Fragment() {

    lateinit var pharmaList: List<Pharmacie>
    lateinit var pharmaAdapter : PharmaciesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.pharmacies_ville_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Pharmacie_recycler.layoutManager = LinearLayoutManager(this.activity)
        getPharmacies()

        pharmaAdapter.setOnItemClickListener(
            PharmaciesAdapter.OnItemClickListener(){
                fun onClick
            }
        )

    }

    private fun getPharmacies(){

        pharmaList = RoomService.appDatabase.getPharmacieDao().getAllPharmacies()
        pharmaAdapter = PharmaciesAdapter(pharmaList)
        Pharmacie_recycler.adapter = pharmaAdapter
    }

}
