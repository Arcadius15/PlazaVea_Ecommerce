package edu.pe.idat.pva.providers

import edu.pe.idat.pva.api.ApiRoutes
import edu.pe.idat.pva.models.Categoria
import edu.pe.idat.pva.models.SubCategoriaItem
import edu.pe.idat.pva.routes.CategoriaRoutes
import retrofit2.Call

class CategoriaProvider(val token: String) {
    private var  categoriaRoutes: CategoriaRoutes? = null

    init{
        val api = ApiRoutes()
        categoriaRoutes = api.getCategorias(token)
    }

    fun getAll(): Call<ArrayList<Categoria>>? {
        return categoriaRoutes?.getAll(token)

    }
}