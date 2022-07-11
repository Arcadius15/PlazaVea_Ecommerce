package edu.pe.idat.pva.api

import edu.pe.idat.pva.routes.CategoriaRoutes
import edu.pe.idat.pva.routes.ProductsRoutes
import edu.pe.idat.pva.routes.UserRoutes
import edu.pe.idat.pva.routes.UsuarioRoutes

class ApiRoutes {

    val API_URL = "puerto"
    val HEROKU_URL= "https://plazavea-webservice.herokuapp.com/"
    val retrofit = RetrofitInstance()

    fun getUsersRoutes(): UserRoutes {
        return retrofit.getRetrofit(API_URL).create(UserRoutes::class.java)
    }

    fun getCategorias(token:String): CategoriaRoutes{
        return retrofit.getRetrofit(API_URL).create(CategoriaRoutes::class.java)
    }

    fun getProducts(token:String): ProductsRoutes{
        return retrofit.getRetrofit(API_URL).create(ProductsRoutes::class.java)
    }

    fun getUsuarioRoutes(): UsuarioRoutes{
        return retrofit.getRetrofit(HEROKU_URL).create(UsuarioRoutes::class.java)
    }
}