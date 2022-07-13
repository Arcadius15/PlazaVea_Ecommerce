package edu.pe.idat.pva.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.pva.apirep.ProductoApiRepository
import edu.pe.idat.pva.models.Producto
import retrofit2.Call
import retrofit2.http.Path

class ProductsProvider() : ViewModel() {
    var productResponse: LiveData<ArrayList<Producto>>

    private var repository = ProductoApiRepository()

    init{
       productResponse = repository.productoResponse
    }

    fun findByCategory(@Path("idSubcategoria") idSubcategoria: String)  {
         productResponse = repository.findByCategory(idSubcategoria)
    }
}