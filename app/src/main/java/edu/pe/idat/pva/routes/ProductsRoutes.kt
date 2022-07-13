package edu.pe.idat.pva.routes


import edu.pe.idat.pva.models.Product
import edu.pe.idat.pva.models.Producto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProductsRoutes {

    @GET("producto/subcategoria/{idSubcategoria}")
    fun findByCategory(
        @Path("idSubcategoria") idSubcategoria: String
    ): Call<ArrayList<Producto>>
}