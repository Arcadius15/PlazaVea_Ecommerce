package edu.pe.idat.pva.api

import edu.pe.idat.pva.routes.CategoriaRoutes
import edu.pe.idat.pva.routes.UsuarioRoutes
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstanceCreate {
    private const val HEROKU_URL= "https://plazavea-webservice.herokuapp.com/"

    private var okHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.MINUTES)
        .readTimeout(60, TimeUnit.MINUTES)
        .writeTimeout(30, TimeUnit.MINUTES)
        .build()

    private fun buildRetrofit() = Retrofit.Builder()
        .baseUrl(HEROKU_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val getUsuarioRoutes : UsuarioRoutes by lazy {
        buildRetrofit().create(UsuarioRoutes::class.java)
    }

    val getCategoriasRoutes: CategoriaRoutes by lazy {
        buildRetrofit().create(CategoriaRoutes::class.java)
    }
}