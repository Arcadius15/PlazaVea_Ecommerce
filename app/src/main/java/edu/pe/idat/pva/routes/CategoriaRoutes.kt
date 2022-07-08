package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.Categoria
import edu.pe.idat.pva.models.SubCategoriaItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface CategoriaRoutes {

    @GET("categories/getAll")
    fun getAll(
        @Header("Authorization") token: String
    ): Call<ArrayList<Categoria>>
}