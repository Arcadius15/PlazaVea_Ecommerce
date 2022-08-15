package edu.pe.idat.pva.apirep

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.pva.api.RetrofitInstanceCreate
import edu.pe.idat.pva.models.response.TiendaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TiendaApiRepository {

    var lstTiendaResponse = MutableLiveData<ArrayList<TiendaResponse>>()

    fun getTiendas() : MutableLiveData<ArrayList<TiendaResponse>> {
        val call: Call<ArrayList<TiendaResponse>> = RetrofitInstanceCreate
            .getTiendaRoutes.getTiendas()
        call.enqueue(object : Callback<ArrayList<TiendaResponse>>{
            override fun onResponse(
                call: Call<ArrayList<TiendaResponse>>,
                response: Response<ArrayList<TiendaResponse>>
            ) {
                lstTiendaResponse.value = response.body()
            }

            override fun onFailure(call: Call<ArrayList<TiendaResponse>>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }
        })

        return lstTiendaResponse
    }

}