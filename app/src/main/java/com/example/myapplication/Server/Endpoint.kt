package com.example.myapplication.Server

import com.example.myapplication.Entity.Commande
import com.example.myapplication.Entity.Pharmacie
import com.example.myapplication.Entity.User
import com.example.myapplication.Identity.Identity
import retrofit2.Call
import retrofit2.http.*

interface Endpoint {

    @POST("login")
    fun login(@Query("tel") tel: String, @Query("mdp") mdp: String): Call<Identity>

    @POST("adduser")
    fun addUser(@Body user: User): Call<String>

    @GET("getpharmacies")
    fun getPharmacies(): Call<List<Pharmacie>>

    @GET("getpharmaciesparville/{ville}")
    fun getPharmaciesParVille(@Path("ville")ville: String ): Call<List<Pharmacie>>

    @GET("getpharmacie/{id}")
    fun getPharmacie(@Path("id")id : Int): Call<List<Pharmacie>>

    @POST("addcommande")
    fun addCommande(@Body commande: Commande): Call<String>

    @POST("deletecommande")
    fun deleteCommande(@Body commande: Commande): Call<String>

    @POST("updatecommande")
    fun updateCommande(@Body commande: Commande): Call<String>

    @GET("/getcommandesparphar/{idphar}")
    fun getCommandesParPharmacie(@Path("idphar") idphar: Int): Call<List<Commande>>

    // TODO after authentication
    @GET("/getCommandes")
    fun getCommandes()
}