package com.example.myapplication.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapters.OnItemClickListener
import com.example.myapplication.Adapters.PharmaciesAdapter
import com.example.myapplication.Adapters.addOnItemClickListener
import com.example.myapplication.Entity.Pharmacie
import com.example.myapplication.LocalStorage.RoomService
import com.example.myapplication.R
import kotlinx.android.synthetic.main.pharmacies_ville_fragment.*

class PharmaciesVilleFragment : Fragment() {

    lateinit var pharmaList: List<Pharmacie>
    var displayPharmaList = mutableListOf<Pharmacie>()
    lateinit var pharmaAdapter : PharmaciesAdapter
    lateinit var villeAdapter : ArrayAdapter<String>

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

        // when selecting a specific "ville"
        villeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val newList = mutableListOf<Pharmacie>()
                val villeSelected : String = parent?.getItemAtPosition(position).toString()

                // filter recyclerview
                if(villeSelected == "Toutes les villes"){
                    newList.addAll(pharmaList)
                }
                else{
                    pharmaList.forEach {
                        if(it.ville == villeSelected)
                            newList.add(it)
                    }
                }
                Log.d("newList", newList.toString())
                displayPharmaList.clear()
                displayPharmaList.addAll(newList)
                Pharmacie_recycler.adapter?.notifyDataSetChanged()

            }
        }

    }

    private fun setSpinnerElements(){

        val pharmaVilles = mutableListOf<String>()

        pharmaVilles.add("Toutes les villes")
        for( pharmacie in pharmaList){
            if(! pharmaVilles.contains(pharmacie.ville))
                pharmaVilles.add(pharmacie.ville)
        }

        villeAdapter = ArrayAdapter(this@PharmaciesVilleFragment.activity,
            R.layout.dropdown_menu_popup_item, pharmaVilles)
        villeSpinner.setAdapter(villeAdapter)

    }

    private fun getPharmacies(){

        pharmaList = RoomService.appDatabase.getPharmacieDao().getAllPharmacies()

        if(pharmaList.size == 0){
            // TODO : handle case of anonymous user by getting pharmaList from server
        }
        // set elements inside spinner
        setSpinnerElements()
        displayPharmaList.addAll(pharmaList)
        pharmaAdapter = PharmaciesAdapter(displayPharmaList)
        Pharmacie_recycler.adapter = pharmaAdapter
    }

}
