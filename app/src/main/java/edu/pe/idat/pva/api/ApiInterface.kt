package edu.pe.idat.pva.api

import edu.pe.idat.pva.models.SubCategoriaItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("subcategoria")
    fun getData(): Call<List<SubCategoriaItem>>
}