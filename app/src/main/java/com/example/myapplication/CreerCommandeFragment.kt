package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.myapplication.Entity.Commande
import com.example.myapplication.File.UriFileManager
import com.example.myapplication.Identity.MyIdentity
import com.example.myapplication.LocalStorage.RoomService
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

    var uriOrdonnance : Uri? = null

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
                uploadImage()
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
                    Toast.makeText(this@CreerCommandeFragment.activity,"Permission refusée", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            // Saving URI of image
            uriOrdonnance = data?.data
            // Displaying image
            ordonnanceImage.setImageURI(uriOrdonnance)

        }
    }

    /*
    private fun getRealPathFromURI(context: Context, contentUri: Uri ): String {

    var cursor : Cursor? = null
    try {
        var proj =  {MediaStore.Images.Media.DATA}
        cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    } catch (Exception e) {
        Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
        return "";
    } finally {
        if (cursor != null) {
            cursor.close();
        }
    }
}*/
    private fun uploadImage(){
        /**NEWLY ADDED
        var cr = this@CreerCommandeFragment.activity!!.getContentResolver()
        var projection = arrayOf(MediaStore.MediaColumns.DATA)
        var cur = cr.query(Uri.parse(uriOrdonnance), projection, null, null, null);
Cursor
if(cur != null) {
    cur.moveToFirst();
    String filePath = cur.getString(0);
    File imageFile = new File(filePath);
    if(imageFile.exists()) {
        // do something if it exists
        entity.addPart("image", new FileBody(imageFile));
    }
    else {
        // File was not found
        Log.e("MMP","Image not Found");
    }
}
else {
    // content Uri was invalid or some other error occurred
    Log.e("MMP","Invalid content or some error occured");
}
        END NEWLY*/
        var path = UriFileManager.getRealPathFromURI(uriOrdonnance)
        var file = File(path)

        Log.d("File name", file.name)
        Log.d("File path", file.absolutePath)
        var requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        var body = MultipartBody.Part.createFormData("myFile", file.name, requestFile)

        var descriptionString = "hello, this is Nadji"
        var description = RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString)

        Log.d("MyLog", "whyyyy  "+requestFile.toString())

        // request
        var call = RetrofitService.endpoint.upload(description, body)
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
                Log.d("hhhh",t.toString())
                Toast.makeText(this@CreerCommandeFragment.activity, "From onFailure", Toast.LENGTH_LONG).show()
            }})
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
