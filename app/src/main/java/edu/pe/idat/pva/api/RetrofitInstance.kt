package edu.pe.idat.pva.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    val BASE_URL = "https://plazavea-webservice.herokuapp.com/"

    val api: ApiInterface by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)
    }


}