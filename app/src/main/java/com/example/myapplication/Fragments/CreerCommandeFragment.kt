package com.example.myapplication.Fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Entity.Commande
import com.example.myapplication.Entity.Pharmacie
import com.example.myapplication.File.UriFileManager
import com.example.myapplication.Identity.MyIdentity
import com.example.myapplication.LocalStorage.RoomService
import com.example.myapplication.R
import com.example.myapplication.Server.RetrofitService
import kotlinx.android.synthetic.main.creer_commande_fragment.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CreerCommandeFragment : Fragment() {

    lateinit var nomCommande: String
    lateinit var nomPharma: String
    lateinit var villePharma: String
    lateinit var pharmaList: List<Pharmacie>

    var idPharma = -1
    var uriOrdonnance : Uri? = null
    var file : File? = null

    val pharmaNames = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.creer_commande_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpinnerElements()

        arguments?.let {
            val safeArgs = PharmacieFragmentArgs.fromBundle(it)
            idPharma = safeArgs.idPharma
        }

        if(idPharma != -1){
            pharmacieEdit.setText(pharmaList.get(idPharma - 1).nom+" - "+pharmaList.get(idPharma - 1).ville)
        }


        backUp.setOnClickListener{
            findNavController().navigateUp()
        }

        ordonnanceImage.setOnClickListener {

            // check for runtime permission
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if( checkSelfPermission(RoomService.context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_DENIED){
                    // Permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    // show popup to request runtime permission
                    requestPermissions(permissions,
                        PERMISSION_CODE
                    )
                }
                else{
                    // already granted
                    pickImageFromGallery()
                }
            }
            else{
                // system os <= Marshmallow
            }
        }

        OrderButton.setOnClickListener {
            if( checkFields()){
                uploadImage()
            }
        }
    }


    private fun setSpinnerElements(){

        pharmaList = RoomService.appDatabase.getPharmacieDao().getAllPharmacies()

        for( pharmacie in pharmaList){
            pharmaNames.add(pharmacie.nom+" - "+pharmacie.ville)
        }

        val adapter = ArrayAdapter(this@CreerCommandeFragment.activity,
            R.layout.support_simple_spinner_dropdown_item, pharmaNames)

        pharmacieEdit.setAdapter(adapter)
    }


    private fun checkFields(): Boolean {

        if( pharmacieEdit.text.toString() == ""
            || CommandeNameEdit.text.toString() == ""){

            Toast.makeText(this@CreerCommandeFragment.activity, "Veuillez renseigner tous les champs", Toast.LENGTH_SHORT).show()
            return false
        }

        if (file == null){

            Toast.makeText(this@CreerCommandeFragment.activity, "Veuillez uploader votre ordonnance", Toast.LENGTH_SHORT).show()
            return false
        }

        nomCommande = CommandeNameEdit.text.toString()
        var str = pharmacieEdit.text.toString()

        val part = str.split(" ")

        nomPharma = part.get(0)
        villePharma = part.get(2) // the pattern is "nom - ville"
        if(part.size == 4){
            nomPharma = part.get(0) + " " + part.get(1)
            villePharma = part.get(3) // the pattern is "nom1 nom2 - ville"
        }
        Log.d("NOMPHARMA", nomPharma)

        Log.d("Parts", "nom = "+nomPharma+", ville = "+villePharma)

        return true

    }

    private fun pickImageFromGallery() {
        // intent to pick image
        val intent = Intent(Intent.ACTION_PICK)

        intent.type = "image/*"
        startActivityForResult(intent,
            IMAGE_PICK_CODE
        )
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
                    Toast.makeText(this@CreerCommandeFragment.activity,"Permission refusée", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            // Saving URI of image
            uriOrdonnance = data?.data
            var path = UriFileManager.getRealPathFromURI(uriOrdonnance)
            file = File(path)

            // Displaying image
            ordonnanceImage.setImageURI(uriOrdonnance)

        }
    }

    private fun uploadImage(){

        Log.d("File name", file!!.name)
        Log.d("File path", file!!.absolutePath)
        var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)

        val filename = Date().time.toString()+file!!.name
        Log.d("New filename", filename)
        var body = MultipartBody.Part.createFormData("myFile", filename, requestFile)

        var descriptionString = "uploads"
        var description = RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString)

        // request
        var call = RetrofitService.endpoint.upload(description, body)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response:
            Response<String>?) {

                if(response?.isSuccessful!!) {

                    Log.d("upload success", response.body())

                    var idPharma = RoomService.appDatabase.getPharmacieDao().getIDPharmacie(nomPharma, villePharma).get(0)
                    val commande = Commande(0, nomCommande, MyIdentity.getUserTel(), idPharma, nomPharma, villePharma, filename, SimpleDateFormat("dd MMM yyyy").format(Date()), "En attente")

                    creerCommande(commande)
                }
                else
                {
                    Log.d("MyLog", "code error = "+response.code())
                    Toast.makeText(this@CreerCommandeFragment.activity,"Oops, une erreur s'est produite", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                Log.d("hhhh",t.toString())
                Toast.makeText(this@CreerCommandeFragment.activity, "Réseau non disponible !", Toast.LENGTH_LONG).show()
            }})
    }



    private fun creerCommande(commande: Commande){

        val call = RetrofitService.endpoint.addCommande(commande)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response:
            Response<String>?) {

                if(response?.isSuccessful!!) {

                    Log.d("MyLog", response.body())
                    Toast.makeText(this@CreerCommandeFragment.activity,"Votre commande a été créee avec succès", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                }
                else
                {
                    Log.d("MyLog", "code error = "+response.code())
                    Toast.makeText(this@CreerCommandeFragment.activity,"Erreur du serveur", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                Log.d("OnFailure",t.toString())
                Toast.makeText(this@CreerCommandeFragment.activity, "Réseau non disponible !", Toast.LENGTH_LONG).show()
            }})
    }
}
