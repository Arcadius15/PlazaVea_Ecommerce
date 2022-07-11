package edu.pe.idat.pva.apirep

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.pva.api.RetrofitInstanceCreate
import edu.pe.idat.pva.models.SubCategoriaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriaApiRepository {
    var subCategoriaResponse = MutableLiveData<ArrayList<SubCategoriaResponse>>()

    fun getAll() : MutableLiveData<ArrayList<SubCategoriaResponse>> {
        val call: Call<ArrayList<SubCategoriaResponse>> = RetrofitInstanceCreate
            .getCategoriasRoutes.getAll()
        call.enqueue(object : Callback<ArrayList<SubCategoriaResponse>>{
            override fun onResponse(
                call: Call<ArrayList<SubCategoriaResponse>>,
                response: Response<ArrayList<SubCategoriaResponse>>
            ) {
                subCategoriaResponse.value = response.body()
            }

            override fun onFailure(call: Call<ArrayList<SubCategoriaResponse>>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }

        })

        return subCategoriaResponse
    }
}