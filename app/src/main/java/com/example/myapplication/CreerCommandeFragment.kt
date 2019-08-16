package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
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

        uploadImage.setOnClickListener {

            // check for runtime permission
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if( checkSelfPermission(RoomService.context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED){
                    // Permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    // show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    // already granted
                    pickImageFromGallery()
                }
            }
            else{
                // system os <= Marshmallow
            }

            //            nomCommande = "nouvelle Commande"
//            nomPharma = "Les Roses"
//            villePharma = "Blida"
//
//            creerCommande()
        }

        lancerCommande.setOnClickListener {
            if( checkFields()){

            }
        }
    }

    private fun checkFields(): Boolean {

        // TODO : UI interaction in case a field is missing or image is empty
        return true

    }

    private fun pickImageFromGallery() {
        // intent to pick image
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        // image pick code
        private val IMAGE_PICK_CODE = 1000
        // Permission code
        private val PERMISSION_CODE = 1001

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if( grantResults.size > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    // permission granted from popup
                    pickImageFromGallery()
                }
                else{
                    // permission denied from popup
                    Toast.makeText(this@CreerCommandeFragment.activity,"Permission denied", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            ordonnanceImage.setImageURI(data?.data)
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
