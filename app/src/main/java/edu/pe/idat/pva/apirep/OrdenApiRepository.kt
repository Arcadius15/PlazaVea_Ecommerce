package edu.pe.idat.pva.apirep

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.pva.api.RetrofitInstanceCreate
import edu.pe.idat.pva.models.request.OrdenHistorialRequest
import edu.pe.idat.pva.models.request.OrdenPatchRequest
import edu.pe.idat.pva.models.request.OrdenRequest
import edu.pe.idat.pva.models.response.OrdenPageResponse
import edu.pe.idat.pva.models.response.OrdenResponse
import edu.pe.idat.pva.models.response.RepartidorResponse
import edu.pe.idat.pva.models.response.ResponseHttp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdenApiRepository {

    var ordenId = MutableLiveData<String>()
    var responseHttp = MutableLiveData<ResponseHttp>()
    var ordenResponse = MutableLiveData<OrdenResponse>()
    var ordenPageResponse = MutableLiveData<OrdenPageResponse>()
    var repartidorResponse = MutableLiveData<RepartidorResponse>()

    fun registrarOrden(ordenRequest: OrdenRequest, token: String) : MutableLiveData<String> {
        val call: Call<String> = RetrofitInstanceCreate
            .getOrdenRoutes.registrarOrden(ordenRequest, token)
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    ordenId.value = response.body()
                } else {
                    ordenId.value = "error"
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                ordenId.value = "error"
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

    fun getAllByCliente(idCliente: String, page: Int, token: String) : MutableLiveData<OrdenPageResponse> {
        val call: Call<OrdenPageResponse> = RetrofitInstanceCreate
            .getOrdenRoutes.getAllByCliente(idCliente, page, token)
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

    fun actualizarOrden(idOrden: String, ordenPatchRequest: OrdenPatchRequest, token: String) : MutableLiveData<ResponseHttp> {
        val call: Call<Void> = RetrofitInstanceCreate
            .getOrdenRoutes.actualizarOrden(idOrden,ordenPatchRequest,token)
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

    fun getRepartidor(idRepartidor: String, token: String) : MutableLiveData<RepartidorResponse> {
        val call: Call<RepartidorResponse> = RetrofitInstanceCreate
            .getOrdenRoutes.getRepartidor(idRepartidor, token)
        call.enqueue(object: Callback<RepartidorResponse>{
            override fun onResponse(
                call: Call<RepartidorResponse>,
                response: Response<RepartidorResponse>
            ) {
                repartidorResponse.value = response.body()
            }

            override fun onFailure(call: Call<RepartidorResponse>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }
        })

        return repartidorResponse
    }
}