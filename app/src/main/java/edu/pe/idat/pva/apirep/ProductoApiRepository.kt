package edu.pe.idat.pva.apirep

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.pva.api.RetrofitInstanceCreate
import edu.pe.idat.pva.models.Producto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductoApiRepository {

    var productoResponse = MutableLiveData<ArrayList<Producto>>()


    fun findByCategory(idSubcategoria: String): LiveData<ArrayList<Producto>> {
        val call: Call<ArrayList<Producto>> = RetrofitInstanceCreate
            .getProductsRoutes.findByCategory(idSubcategoria)
        call.enqueue(object : Callback<ArrayList<Producto>>{
            override fun onResponse(call: Call<ArrayList<Producto>>, response: Response<ArrayList<Producto>>
            ) {
               productoResponse.value = response.body()
            }

            override fun onFailure(call: Call<ArrayList<Producto>>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }
        } )

        return productoResponse
    }





   

}