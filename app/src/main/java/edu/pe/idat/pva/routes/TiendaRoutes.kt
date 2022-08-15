package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.response.TiendaResponse
import retrofit2.Call
import retrofit2.http.GET

interface TiendaRoutes {

    @GET("tienda")
    fun getTiendas(): Call<ArrayList<TiendaResponse>>

}