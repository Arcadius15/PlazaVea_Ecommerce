package edu.pe.idat.pva.providers

import edu.pe.idat.pva.api.ApiRoutes
import edu.pe.idat.pva.models.Product
import edu.pe.idat.pva.routes.ProductsRoutes
import retrofit2.Call

class ProductsProvider(val token: String) {
    private var  productsRoutes: ProductsRoutes? = null

    init{
        val api = ApiRoutes()
        productsRoutes = api.getProducts(token)
    }

    fun findByCategory(idCategory: String): Call<ArrayList<Product>>? {
        return productsRoutes?.findByCategory(idCategory, token)

    }
}