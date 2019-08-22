package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Identity.MyIdentity
import com.example.myapplication.Identity.TelMdp
import com.example.myapplication.Server.RetrofitService
import kotlinx.android.synthetic.main.reinit_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReinitFragment : Fragment() {


    var passwordField = ""
    var confirmField = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.reinit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ReinitButton.setOnClickListener {
            passwordField = NewPasswordEdit.text.toString()
            confirmField = ConfirmEdit.text.toString()

            if (passwordField == confirmField) {
                reinit()
            }
            else
            {
                //TODO : interact with user to let him know they re not the same
                Toast.makeText(this@ReinitFragment.activity,"Les deux champs ne correspondent pas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun reinit(){

        val call = RetrofitService.endpoint.reinit( TelMdp(MyIdentity.getUserTel(), passwordField))

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response:
            Response<String>?) {

                if(response?.isSuccessful!!) {
                    Toast.makeText(this@ReinitFragment.activity,"PasswordReinit", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_reinit_to_commandes, null)
                }
                else
                {
                    Log.d("NotSuccessful-response",response.toString())
                    Toast.makeText(this@ReinitFragment.activity,"Erreur interne au serveur", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                Log.d("OnFailure",t.toString())
                Toast.makeText(this@ReinitFragment.activity, "Réseau non disponible", Toast.LENGTH_LONG).show()
            }})
    }
}
