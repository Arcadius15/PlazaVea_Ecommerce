package edu.pe.idat.pva.routes


import edu.pe.idat.pva.models.response.Producto
import edu.pe.idat.pva.models.response.ProductosCategoriaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsRoutes {

    @GET("producto/subcategoria/{idSubcategoria}")
    fun findByCategory(
        @Path("idSubcategoria") idSubcategoria: String,
        @Query("page") page: Int
    ): Call<ProductosCategoriaResponse>

    @GET("producto/{idProducto}")
    fun findById(
        @Path("idProducto") idProducto: String
    ): Call<Producto>
}