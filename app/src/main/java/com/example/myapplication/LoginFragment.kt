package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.myapplication.Entity.Commande
import com.example.myapplication.Entity.Pharmacie
import com.example.myapplication.Entity.User
import com.example.myapplication.Identity.Identity
import com.example.myapplication.Identity.MyIdentity
import com.example.myapplication.Identity.TelMdp
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
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LoginButton.setOnClickListener {
            telField = phoneEdit.text.toString()
            passwordField = passwordEdit.text.toString()

            if( checkField())
                login()
        }

        CreateAccountButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register, null)
        }
    }

    private fun checkField(): Boolean {

        // TODO check if fields are not empty, and display warnings if they are
        return true
    }

    private fun login(){

        val call = RetrofitService.endpoint.login(TelMdp(telField, passwordField))

        call.enqueue(object : Callback<Identity> {
            override fun onResponse(call: Call<Identity>?, response:
            Response<Identity>?) {

                if(response?.isSuccessful!!) {
                    MyIdentity.user = response.body()?.user
                    MyIdentity.token = response.body()?.token
                    // Go to commande activity
                    Log.d("TOKEN",MyIdentity.token)
                    Log.d("User",response.body().toString())
                    if(MyIdentity.user?.toReinit == 0 )
                        findNavController().navigate(R.id.action_login_to_commandes, null)
                    else
                        findNavController().navigate(R.id.action_login_to_reinit, null)

                    //Toast.makeText(this@LoginFragment.activity,"From isSuccessful", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Log.d("NotSuccessful-response",response.message())
                    Toast.makeText(this@LoginFragment.activity,"Téléphone ou mot de passe incorrects", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Identity>?, t: Throwable?) {
                Log.d("MyLog",t.toString())
                Toast.makeText(this@LoginFragment.activity, "Réseau non disponible", Toast.LENGTH_LONG).show()
            }})

    }


    // TODO : adapt it to its fragment when copied
    private fun getCommandeParPharmacie(idPharmacie : Int){

        val call = RetrofitService.endpoint.getCommandesParPharmacie(idPharmacie)
        call.enqueue(object : Callback<List<Commande>> {
            override fun onResponse(call: Call<List<Commande>>?, response: Response<List<Commande>>?) {

                if(response?.isSuccessful!!) {

                    Log.d("Nice",response.body().toString())
                    val list : List<Commande>? = response.body()

                    if (list!!.size >= 1) {

                      /*  nom1.text = list.get(0)?.id.toString()
                        ville1.text = list.get(0)?.dateEmission
                        if (list.size >= 2 ) {
                            nom2.text = list.get(1)?.id.toString()
                            ville2.text = list.get(1)?.dateEmission
                        }
                        else{
                            nom2.text = "Only one"
                            ville2.text = "Only one"
                        }*/
                    }

                } else {
                    Log.d("OnResponse_NOTSuccss",response.toString())
                    Toast.makeText(this@LoginFragment.activity, "From OnResponse NOT Successful", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Commande>>?, t: Throwable?) {
                Log.d("OnFailure", t.toString())
                Toast.makeText(this@LoginFragment.activity, "Réseau non disponible", Toast.LENGTH_SHORT).show()
            }})
    }

    // TODO : adapt it to its fragment when copied
    private fun updateCommande(commande: Commande){

        val call = RetrofitService.endpoint.updateCommande(commande)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response:
            Response<String>?) {

                if(response?.isSuccessful!!) {

                    Toast.makeText(this@LoginFragment.activity,response.body()+"From isSuccessful", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this@LoginFragment.activity,"From Not Succesful", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                Log.d("OnFailure",t.toString())
                Toast.makeText(this@LoginFragment.activity, "From onFailure", Toast.LENGTH_LONG).show()
            }})
    }

    // TODO : adapt it to its fragment when copied
    private fun deleteCommande(commande: Commande){

        val call = RetrofitService.endpoint.deleteCommande(commande)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response:
            Response<String>?) {

                if(response?.isSuccessful!!) {

                    Toast.makeText(this@LoginFragment.activity,response.body()+"From isSuccessful", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this@LoginFragment.activity,"From Not Succesful", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                Log.d("OnFailure",t.toString())
                Toast.makeText(this@LoginFragment.activity, "From onFailure", Toast.LENGTH_LONG).show()
            }})
    }


    // TODO : adapt it to its fragment when copied
    private fun addCommande(commande: Commande){

        val call = RetrofitService.endpoint.addCommande(commande)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response:
            Response<String>?) {

                if(response?.isSuccessful!!) {

                    Toast.makeText(this@LoginFragment.activity,response.body()+"From isSuccessful", Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this@LoginFragment.activity,"From Not Succesful", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<String>?, t: Throwable?) {
                Log.d("OnFailure",t.toString())
                Toast.makeText(this@LoginFragment.activity, "From onFailure", Toast.LENGTH_LONG).show()
            }})
    }


    // TODO : adapt it to its fragment when copied
    private fun getPharmacie(id: Int){

        val call = RetrofitService.endpoint.getPharmacie(id)
        call.enqueue(object : Callback<List<Pharmacie>> {
            override fun onResponse(call: Call<List<Pharmacie>>?, response: Response<List<Pharmacie>>?) {

                if(response?.isSuccessful!!) {

                    val list : List<Pharmacie>? = response.body()

                    if (list != null) {
                        if (list.size == 1){
/*                            nom1.text = list.get(0)?.nom
                            ville1.text = list.get(0)?.ville*/
                        }
                    }

                } else {
                    Toast.makeText(this@LoginFragment.activity, "From OnResponse NOT Successful", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Pharmacie>>?, t: Throwable?) {
                Toast.makeText(this@LoginFragment.activity, "From OnFailure", Toast.LENGTH_SHORT).show()
            }})
    }

    // TODO : adapt it to its fragment when copied
    private fun getPharmaciesParVille( ville: String){

    val call = RetrofitService.endpoint.getPharmaciesParVille(ville)
    call.enqueue(object : Callback<List<Pharmacie>> {
        override fun onResponse(call: Call<List<Pharmacie>>?, response: Response<List<Pharmacie>>?) {

            if(response?.isSuccessful!!) {

                val list : List<Pharmacie>? = response.body()

                if (list != null) {
                    if (list.size == 1){
  /*                      nom1.text = list.get(0)?.nom
                        ville1.text = list.get(0)?.ville*/
                    }
                }

            } else {
                Toast.makeText(this@LoginFragment.activity, "From OnResponse NOT Successful", Toast.LENGTH_SHORT).show()
            }
        }
        override fun onFailure(call: Call<List<Pharmacie>>?, t: Throwable?) {
            Toast.makeText(this@LoginFragment.activity, "From OnFailure", Toast.LENGTH_SHORT).show()
        }})
    }


}
