package com.example.myapplication

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapters.OnItemClickListener
import com.example.myapplication.Adapters.PharmaciesAdapter
import com.example.myapplication.Adapters.addOnItemClickListener
import com.example.myapplication.Entity.Pharmacie
import com.example.myapplication.LocalStorage.RoomService
import kotlinx.android.synthetic.main.pharmacies_ville_fragment.*

class PharmaciesVilleFragment : Fragment() {

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

        Pharmacie_recycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                var idPharmaSelected = pharmaAdapter.pharmaList[position].id
                val nextAction = PharmaciesVilleFragmentDirections.actionVillesToPharmacie()
                nextAction.setIdPharma(idPharmaSelected)
                Log.d("IDPHARMA",idPharmaSelected.toString())

                findNavController().navigate(nextAction, null)
            }
        })
    }

    private fun getPharmacies(){

        pharmaList = RoomService.appDatabase.getPharmacieDao().getAllPharmacies()

        if(pharmaList.size == 0){
            // TODO : handle case of anonymous user by getting pharmaList from server
        }

        pharmaAdapter = PharmaciesAdapter(pharmaList)
        Pharmacie_recycler.adapter = pharmaAdapter
    }

}
