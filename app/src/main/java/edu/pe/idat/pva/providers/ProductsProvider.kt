package edu.pe.idat.pva.providers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.pe.idat.pva.apirep.ProductoApiRepository
import edu.pe.idat.pva.models.response.Producto
import edu.pe.idat.pva.models.response.ProductosCategoriaResponse
import retrofit2.http.Path

class ProductsProvider() : ViewModel() {
    var productResponse: LiveData<ProductosCategoriaResponse>

    private var repository = ProductoApiRepository()

    init{
       productResponse = repository.productoResponse
    }

    fun findByCategory(@Path("idSubcategoria") idSubcategoria: String, page: Int)  {
         productResponse = repository.findByCategory(idSubcategoria,page)
    }

    fun findById(idProducto: String): LiveData<Producto> {
        return repository.findById(idProducto)
    }
}