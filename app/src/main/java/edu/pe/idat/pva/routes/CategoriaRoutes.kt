package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.SubCategoriaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CategoriaRoutes {

    @GET("subcategoria")
    fun getAll(): Call<ArrayList<SubCategoriaResponse>>
}