package edu.pe.idat.pva.routes


import edu.pe.idat.pva.models.response.Producto
import edu.pe.idat.pva.models.response.ProductosCategoriaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductsRoutes {

    @GET("producto/subcategoria/{idSubcategoria}?size=50")
    fun findByCategory(
        @Path("idSubcategoria") idSubcategoria: String
    ): Call<ProductosCategoriaResponse>

    @GET("producto/{idProducto}")
    fun findById(
        @Path("idProducto") idProducto: String
    ): Call<Producto>
}