package com.example.myapplication.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Identity.Identity
import com.example.myapplication.Identity.MyIdentity
import com.example.myapplication.Identity.TelMdp
import com.example.myapplication.R
import com.example.myapplication.Server.RetrofitService
import kotlinx.android.synthetic.main.login_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class LoginFragment : Fragment() {


    var telField = ""
    var passwordField = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.login_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Todo check this
        if( MyIdentity.isLoggedIn()){
            findNavController().navigate(R.id.action_login_to_commandes, null)
        }

        LoginButton.setOnClickListener {
            telField = phoneEdit.text.toString()
            passwordField = passwordEdit.text.toString()

            if( checkField())
                login()
        }

        anonymeButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_villes, null)
        }

        CreateAccountButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register, null)
        }

    }

    private fun checkField(): Boolean {

        if(telField == ""){
            Toast.makeText(activity, "Veuillez entrer un numéro de téléphone valide", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun login(){

        val call = RetrofitService.endpoint.login(TelMdp(telField, passwordField))

        call.enqueue(object : Callback<Identity> {
            override fun onResponse(call: Call<Identity>?, response:
            Response<Identity>?) {

                if(response?.isSuccessful!!) {

                    val user = response.body()?.user
                    val token = response.body()?.token

                    // Storing user infos in shared pref
                    MyIdentity.setUserTel(user!!.tel)
                    MyIdentity.setUserToken(token!!)

                    if(user?.toReinit == 0)
                        findNavController().navigate(R.id.action_login_to_commandes, null)
                    else
                        findNavController().navigate(R.id.action_login_to_reinit, null)

                }
                else
                {
                    Log.d("NotSuccessful-response",response.message())
                    Toast.makeText(this@LoginFragment.activity,"Téléphone ou mot de passe incorrect", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Identity>?, t: Throwable?) {
                Log.d("MyLog",t.toString())
                Toast.makeText(this@LoginFragment.activity, "Réseau non disponible", Toast.LENGTH_LONG).show()
            }})

    }

}
