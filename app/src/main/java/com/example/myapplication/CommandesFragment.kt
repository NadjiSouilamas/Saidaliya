package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.myapplication.Adapters.CommandesAdapter
import com.example.myapplication.Entity.Commande
import com.example.myapplication.Entity.Pharmacie
import com.example.myapplication.Identity.MyIdentity
import com.example.myapplication.LocalStorage.RoomService
import com.example.myapplication.Server.RetrofitService
import kotlinx.android.synthetic.main.commandes_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class CommandesFragment : Fragment() {

    lateinit var commandeList : List<Commande>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.commandes_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Store all pharmacies in local database
            // retrieve them from server
            // store them in local database
      //  if(//local db is empty){
            // TODO : Retrieve everything from server and store Pharmacies in local
            /*
            * 1. Retrieve pharmacies from Server
            * 2. Store them in local
            * 3. Retrieve Commandes from server
            * 4. Display commandes in recyclerview
            * */
        //} else {
            // TODO : Display commandes ( server access )
            /*
            * 1. Access commandes in remote
            * 2. Display them
            * */
        //}


        commandes_recycler.layoutManager = LinearLayoutManager(this.activity)
        initializeCommandeFragment()

        makeCommandeFAB.setOnClickListener{
            findNavController().navigate(R.id.proximite, null)
        }

        gotoPharmacie.setOnClickListener{
            findNavController().navigate(R.id.villes, null)
        }
    }

    private fun initializeCommandeFragment(){
        val pharmacie = RoomService.appDatabase.getPharmacieDao().getPharmacie(1)

        if(pharmacie.isEmpty()){
            // Store Pharmacies in local ( ONLY in first use )
            storePharmaciesInLocal()
        }
        // Get Commande from server
        getCommandesFromServer()
    }

    // Retrieve and store Pharmacies in local
    private fun storePharmaciesInLocal(){

        val call = RetrofitService.endpoint.getPharmacies()
        call.enqueue(object : Callback<List<Pharmacie>>{
            override fun onResponse(call: Call<List<Pharmacie>>?, response: Response<List<Pharmacie>>?) {

                if(response?.isSuccessful!!) {
                    val pharmacies: List<Pharmacie>? = response.body()
                    // Store pharmacies in local
                    for( pharmacie in pharmacies!!){
                        RoomService.appDatabase.getPharmacieDao().addPharmacie(pharmacie)
                        Log.d("MyLog","Pharmacie : "+pharmacie.toString()+" added")
                    }
                }
                else
                {
                    Log.d("Mylog","storePharmaciesLocal : error msg = "+response.toString())
                    Toast.makeText(this@CommandesFragment.activity, "Oops, il y a un problème !", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Pharmacie>>?, t: Throwable?) {
                Log.d("OnFailure",t.toString())
                Toast.makeText(this@CommandesFragment.activity, "Réseau non disponible !", Toast.LENGTH_SHORT).show()
            }})
    }

    // TODO : are we accessing server each time we see commandes ??
    private fun getCommandesFromServer(){

        val call = RetrofitService.endpoint.getCommandes()
        call.enqueue(object : Callback<List<Commande>> {
            override fun onResponse(call: Call<List<Commande>>?, response: Response<List<Commande>>?) {

                if(response?.isSuccessful!!) {
                    // Extracting the list of commandes
                    commandeList = response.body()!!

                    // Making the recycler adapter
                    commandes_recycler.adapter = CommandesAdapter(commandeList!!)

                } else {
                    Log.d("MyLog","GetCommandeServer : error msg = "+response.toString())
                    Toast.makeText(this@CommandesFragment.activity, "NOT Successful", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Commande>>?, t: Throwable?) {
                Log.d("OnFailure", t.toString())
                Toast.makeText(this@CommandesFragment.activity, "Réseau non disponible", Toast.LENGTH_SHORT).show()
            }})
    }
}
