package com.example.myapplication

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.Entity.Commande
import com.example.myapplication.Identity.MyIdentity
import com.example.myapplication.LocalStorage.RoomService
import com.example.myapplication.Server.RetrofitService
import kotlinx.android.synthetic.main.creer_commande_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CreerCommandeFragment : Fragment() {

    lateinit var nomCommande: String
    lateinit var nomPharma: String
    lateinit var villePharma: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.creer_commande_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeCommandeButton.setOnClickListener {
            nomCommande = "nouvelle Commande"
            nomPharma = "Les Roses"
            villePharma = "Blida"

            creerCommande()
        }
    }

    private fun creerCommande(){
        var idPharma = RoomService.appDatabase.getPharmacieDao().getIDPharmacie(nomPharma, villePharma).get(0)

        Log.d("MyLog", "HERE IS ID PHARMACIE 0 = "+ idPharma)
        val call = RetrofitService.endpoint.addCommande(Commande(0, nomCommande, MyIdentity.user!!.tel, idPharma, nomPharma, villePharma, "nameofthefile", SimpleDateFormat("dd MMM yyyy").format(Date()), "En attente"))
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response:
            Response<String>?) {

                if(response?.isSuccessful!!) {

                    Log.d("MyLog", response.body())
                    Toast.makeText(this@CreerCommandeFragment.activity,"From isSuccessful", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Log.d("MyLog", "code error = "+response.code())
                    Toast.makeText(this@CreerCommandeFragment.activity,"From Not Succesful", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                Log.d("OnFailure",t.toString())
                Toast.makeText(this@CreerCommandeFragment.activity, "From onFailure", Toast.LENGTH_LONG).show()
            }})
    }
}
