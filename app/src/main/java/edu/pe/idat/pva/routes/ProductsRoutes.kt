package edu.pe.idat.pva.routes


import edu.pe.idat.pva.models.Producto
import edu.pe.idat.pva.models.ProductosCategoriaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductsRoutes {

    @GET("producto/subcategoria/{idSubcategoria}")
    fun findByCategory(
        @Path("idSubcategoria") idSubcategoria: String
    ): Call<ProductosCategoriaResponse>

    @GET("producto/{idProducto}")
    fun findById(
        @Path("idProducto") idProducto: String
    ): Call<Producto>
}