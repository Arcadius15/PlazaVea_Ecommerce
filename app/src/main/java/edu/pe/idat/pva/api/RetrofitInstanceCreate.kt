package edu.pe.idat.pva.api

import edu.pe.idat.pva.routes.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val getUsuarioRoutes : UsuarioRoutes by lazy {
        buildRetrofit().create(UsuarioRoutes::class.java)
    }

    val getCategoriasRoutes: CategoriaRoutes by lazy {
        buildRetrofit().create(CategoriaRoutes::class.java)
    }

    val getProductsRoutes: ProductsRoutes by lazy {
        buildRetrofit().create(ProductsRoutes::class.java)
    }

    val getClienteRoutes: ClienteRoutes by lazy {
        buildRetrofit().create(ClienteRoutes::class.java)
    }

    val getOrdenRoutes: OrdenRoutes by lazy {
        buildRetrofit().create(OrdenRoutes::class.java)
    }

    val getDireccionRoutes: DireccionRoutes by lazy {
        buildRetrofit().create(DireccionRoutes::class.java)
    }

    val getTarjetaRoutes: TarjetaRoutes by lazy {
        buildRetrofit().create(TarjetaRoutes::class.java)
    }

    val getRucRoutes: RucRoutes by lazy {
        buildRetrofit().create(RucRoutes::class.java)
    }
}