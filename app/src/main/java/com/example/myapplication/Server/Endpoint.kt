package com.example.myapplication.Server

import com.example.myapplication.Entity.Commande
import com.example.myapplication.Entity.Pharmacie
import com.example.myapplication.Entity.User
import com.example.myapplication.Identity.Identity
import com.example.myapplication.Identity.TelMdp
import retrofit2.Call
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.http.Multipart



interface Endpoint {

    @POST("login")
    fun login(@Body telmdp: TelMdp): Call<Identity>

    @POST("/reinit")
    fun reinit(@Body telmdp: TelMdp): Call<String>

    @POST("adduser")
    fun addUser(@Body user: User): Call<String>


    @GET("/getCommandes")
    fun getCommandes(): Call<List<Commande>>


    @GET("getpharmacies")
    fun getPharmacies(): Call<List<Pharmacie>>

    @POST("addcommande")
    fun addCommande(@Body commande: Commande): Call<String>

    @Multipart
    @POST("upload")
    fun upload(
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<String>

    @POST("deletecommande")
    fun deleteCommande(@Body commande: Commande): Call<String>

    @POST("updatecommande")
    fun updateCommande(@Body commande: Commande): Call<String>

    @GET("/getcommandesparphar/{idphar}")
    fun getCommandesParPharmacie(@Path("idphar") idphar: Int): Call<List<Commande>>

}