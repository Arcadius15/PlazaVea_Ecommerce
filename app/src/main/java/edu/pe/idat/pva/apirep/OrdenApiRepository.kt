package edu.pe.idat.pva.apirep

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.pva.api.RetrofitInstanceCreate
import edu.pe.idat.pva.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdenApiRepository {

    var ordenId = MutableLiveData<String>()
    var responseHttp = MutableLiveData<ResponseHttp>()
    var ordenResponse = MutableLiveData<OrdenResponse>()
    var ordenPageResponse = MutableLiveData<OrdenPageResponse>()

    fun registrarOrden(ordenRequest: OrdenRequest, token: String) : MutableLiveData<String> {
        val call: Call<String> = RetrofitInstanceCreate
            .getOrdenRoutes.registrarOrden(ordenRequest, token)
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                try{
                    if(response.isSuccessful){
                        ordenId.value = response.body()
                    } else {
                        ordenId.value = "error"
                    }
                } catch (e: Exception){
                    e.printStackTrace()
                    ordenId.value = "error"
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }

        })

        return ordenId
    }

    fun registrarHistorial(ordenHistorialRequest: OrdenHistorialRequest, token: String) : MutableLiveData<ResponseHttp> {
        val call: Call<Void> = RetrofitInstanceCreate
            .getOrdenRoutes.registrarHistorial(ordenHistorialRequest, token)
        call.enqueue(object : Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful){
                    responseHttp.value = ResponseHttp(
                        "Éxito",
                        response.isSuccessful,
                        "Correcto",
                        "No"
                    )
                } else {
                    responseHttp.value = ResponseHttp(
                        "Error",
                        response.isSuccessful,
                        "Problema",
                        "Sí"
                    )
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }

        })

        return responseHttp
    }

    fun getOrden(idOrden: String, token: String) : MutableLiveData<OrdenResponse> {
        val call: Call<OrdenResponse> = RetrofitInstanceCreate
            .getOrdenRoutes.getOrden(idOrden, token)
        call.enqueue(object : Callback<OrdenResponse>{
            override fun onResponse(call: Call<OrdenResponse>, response: Response<OrdenResponse>) {
                ordenResponse.value = response.body()
            }

            override fun onFailure(call: Call<OrdenResponse>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }

        })

        return ordenResponse
    }

    fun getAllByCliente(idCliente: String, token: String) : MutableLiveData<OrdenPageResponse> {
        val call: Call<OrdenPageResponse> = RetrofitInstanceCreate
            .getOrdenRoutes.getAllByCliente(idCliente, token)
        call.enqueue(object : Callback<OrdenPageResponse>{
            override fun onResponse(call: Call<OrdenPageResponse>, response: Response<OrdenPageResponse>) {
                ordenPageResponse.value = response.body()
            }

            override fun onFailure(call: Call<OrdenPageResponse>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }

        })

        return ordenPageResponse
    }
}