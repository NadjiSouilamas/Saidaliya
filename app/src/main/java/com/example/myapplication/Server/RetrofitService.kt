package com.example.myapplication.Server

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    val endpoint :Endpoint by lazy {
        // TODO : Put base URL
        Retrofit.Builder().baseUrl("http://192.168.1.5:8082").
            addConverterFactory(GsonConverterFactory.create()).
            build().create(Endpoint::class.java)
    }
}
