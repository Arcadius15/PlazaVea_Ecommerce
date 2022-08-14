package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.response.SubCategoriaResponse
import retrofit2.Call
import retrofit2.http.GET

interface CategoriaRoutes {

    @GET("subcategoria")
    fun getAll(): Call<ArrayList<SubCategoriaResponse>>
}