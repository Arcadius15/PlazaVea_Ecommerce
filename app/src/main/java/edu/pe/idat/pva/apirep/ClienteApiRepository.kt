package edu.pe.idat.pva.apirep

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.pva.api.RetrofitInstanceCreate
import edu.pe.idat.pva.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClienteApiRepository {

    var responseHttp = MutableLiveData<ResponseHttp>()

    fun registrarDireccion(direccionRequest: DireccionRequest, token: String) : MutableLiveData<ResponseHttp> {
        val call: Call<Void> = RetrofitInstanceCreate
            .getClienteRoutes.registrarDireccion(direccionRequest, token)

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

    fun registrarRuc(rucRequest: RucRequest, token: String) : MutableLiveData<ResponseHttp> {
        val call: Call<Void> = RetrofitInstanceCreate
            .getClienteRoutes.registrarRuc(rucRequest, token)

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

    fun registrarTarjeta(tarjetaRequest: TarjetaRequest, token: String) : MutableLiveData<ResponseHttp> {
        val call: Call<Void> = RetrofitInstanceCreate
            .getClienteRoutes.registrarTarjeta(tarjetaRequest, token)

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
}