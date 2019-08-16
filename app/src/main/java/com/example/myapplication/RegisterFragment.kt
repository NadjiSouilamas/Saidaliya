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
import com.example.myapplication.Entity.User
import com.example.myapplication.Server.RetrofitService
import kotlinx.android.synthetic.main.register_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    var nss : Double = 0.0
    var nom = ""
    var prenom = ""
    var adresse = ""
    var tel = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RegisterButton.setOnClickListener {
            tel = "+213558979648"
            nom = "SOUILAMAS"
            prenom = "Amir"
            adresse = "Blida"

            if( checkFields()){
                var user = User(tel, nss, nom, prenom, 1,adresse)
                register(user)
            }
        }
    }

    private fun register(user: User) {

        val call = RetrofitService.endpoint.addUser(user)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response:
            Response<String>?) {

                if(response?.isSuccessful!!) {

                    Toast.makeText(this@RegisterFragment.activity,"From isSuccessful", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Log.d("Not Successful", response.code().toString())
                    Toast.makeText(this@RegisterFragment.activity,"Ce numéro de téléphone est déja pris", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                Log.d("OnFailure",t.toString())
                Toast.makeText(this@RegisterFragment.activity, "OnFailure", Toast.LENGTH_LONG).show()
            }})


    }

    private fun checkFields(): Boolean {
        // TODO Implementation in UI phase
        return true
    }
}
