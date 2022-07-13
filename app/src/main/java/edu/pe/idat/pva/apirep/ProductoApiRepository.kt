package edu.pe.idat.pva.apirep

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.pva.api.RetrofitInstanceCreate
import edu.pe.idat.pva.models.Producto
import edu.pe.idat.pva.models.ProductosCategoriaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductoApiRepository {

    var productoResponse = MutableLiveData<ProductosCategoriaResponse>()


    fun findByCategory(idSubcategoria: String): LiveData<ProductosCategoriaResponse> {
        val call: Call<ProductosCategoriaResponse> = RetrofitInstanceCreate
            .getProductsRoutes.findByCategory(idSubcategoria)
        call.enqueue(object : Callback<ProductosCategoriaResponse>{
            override fun onResponse(call: Call<ProductosCategoriaResponse>, response: Response<ProductosCategoriaResponse>
            ) {
               productoResponse.value = response.body()
            }

            override fun onFailure(call: Call<ProductosCategoriaResponse>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }
        } )

        return productoResponse
    }





   

}