package com.example.myapplication.Server

import com.example.myapplication.Identity.MyIdentity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object RetrofitService {

    val endpoint :Endpoint by lazy {
        // TODO : Put base URL
        val client = OkHttpClient.Builder()

        client.addInterceptor(Interceptor {
             val request : Request = it.request()
             val newRequest = request.newBuilder().header("Authorization", "bearer "+MyIdentity.token)
            it.proceed(newRequest.build())
        })

        Retrofit.Builder().baseUrl("http://192.168.1.4:8082").
            client(client.build()).
            addConverterFactory(GsonConverterFactory.create()).
            build().create(Endpoint::class.java)
    }
}
