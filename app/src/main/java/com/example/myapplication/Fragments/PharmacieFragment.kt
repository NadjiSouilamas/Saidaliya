package com.example.myapplication.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.myapplication.Entity.Pharmacie
import com.example.myapplication.LocalStorage.RoomService
import com.example.myapplication.Server.RetrofitService
import kotlinx.android.synthetic.main.pharmacie_fragment.*
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Identity.MyIdentity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.creer_commande_fragment.*


class PharmacieFragment : Fragment() {

    lateinit var pharmacie : Pharmacie
    var idPharma : Int = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.pharmacie_fragment, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!MyIdentity.isLoggedIn())
            commanderIci.visibility = View.GONE

        arguments?.let {
            val safeArgs = PharmacieFragmentArgs.fromBundle(it)
            idPharma = safeArgs.idPharma
        }

        displayPharmacie()

        fbButton.setOnClickListener{
            startActivity(newFacebookIntent(activity!!.packageManager, pharmacie.facebook))
        }

        mapsButton.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:"+pharmacie.lat+","+pharmacie.lng+"?z=17")
            val mapIntent =  Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        backUp2.setOnClickListener{
            findNavController().navigateUp()
        }

        commanderIci.setOnClickListener{

            val nextAction = PharmacieFragmentDirections.actionPharmacieToCreerCommande()
            nextAction.idPharma = pharmacie.id
            findNavController().navigate(nextAction, null)
        }
    }

    fun newFacebookIntent(pm: PackageManager, url: String): Intent {
        var uri = Uri.parse(url)
        try {
            val applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0)
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=$url")
            }
        } catch (ignored: PackageManager.NameNotFoundException) {
        }

        return Intent(Intent.ACTION_VIEW, uri)
    }

    fun displayPharmacie(){

        pharmacie = RoomService.appDatabase.getPharmacieDao().getPharmacie(idPharma)[0]

        // load image
        Glide
            .with(this@PharmacieFragment.activity!!)
            .load(RetrofitService.imagesPharmacieAdresse+pharmacie.photo)
            .centerCrop()
            .into(imagePharma)

        namePharma.text =  pharmacie.nom
        phonePharma.text = pharmacie.tel
        addressPharma.text = pharmacie.adresse


        if(pharmacie.ouverture == 0
            && pharmacie.fermeture == 24){

            schedulePharma.text = "Ouvert tous les jours 24h/24"
        }
        else{
            schedulePharma.text = "Ouvert tous les jours de "+pharmacie.ouverture+":00 à "+pharmacie.fermeture+":00"
        }

        if( pharmacie.cnas == 0){
            cnas.visibility = View.GONE
        }

        if( pharmacie.casnos == 0){
            casnos.visibility = View.GONE
        }

        if( pharmacie.cnr == 0){
            cnr.visibility = View.GONE
        }

    }
}

