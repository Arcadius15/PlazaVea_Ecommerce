package edu.pe.idat.pva.apirep

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.pva.api.RetrofitInstanceCreate
import edu.pe.idat.pva.models.request.DireccionRequest
import edu.pe.idat.pva.models.request.RucRequest
import edu.pe.idat.pva.models.request.TarjetaRequest
import edu.pe.idat.pva.models.response.DireccionResponse
import edu.pe.idat.pva.models.response.ResponseHttp
import edu.pe.idat.pva.models.response.RucResponse
import edu.pe.idat.pva.models.response.TarjetaResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClienteApiRepository {

    var responseHttp = MutableLiveData<ResponseHttp>()
    var listTarjetas = MutableLiveData<ArrayList<TarjetaResponse>>()
    var listDirecciones = MutableLiveData<ArrayList<DireccionResponse>>()
    var listRuc = MutableLiveData<ArrayList<RucResponse>>()

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

    fun listarTarjetas(idCliente: String, token: String) : MutableLiveData<ArrayList<TarjetaResponse>> {
        val call: Call<ArrayList<TarjetaResponse>> = RetrofitInstanceCreate
            .getClienteRoutes.listarTarjetas(idCliente, token)

        call.enqueue(object : Callback<ArrayList<TarjetaResponse>>{
            override fun onResponse(
                call: Call<ArrayList<TarjetaResponse>>,
                response: Response<ArrayList<TarjetaResponse>>
            ) {
                listTarjetas.value = response.body()
            }

            override fun onFailure(call: Call<ArrayList<TarjetaResponse>>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }

        })

        return listTarjetas
    }

    fun listarDirecciones(idCliente: String, token: String) : MutableLiveData<ArrayList<DireccionResponse>> {
        val call: Call<ArrayList<DireccionResponse>> = RetrofitInstanceCreate
            .getClienteRoutes.listarDirecciones(idCliente, token)

        call.enqueue(object : Callback<ArrayList<DireccionResponse>>{
            override fun onResponse(
                call: Call<ArrayList<DireccionResponse>>,
                response: Response<ArrayList<DireccionResponse>>
            ) {
                listDirecciones.value = response.body()
            }

            override fun onFailure(call: Call<ArrayList<DireccionResponse>>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }

        })

        return listDirecciones
    }

    fun listarRuc(idCliente: String, token: String) : MutableLiveData<ArrayList<RucResponse>> {
        val call: Call<ArrayList<RucResponse>> = RetrofitInstanceCreate
            .getClienteRoutes.listarRuc(idCliente, token)

        call.enqueue(object : Callback<ArrayList<RucResponse>>{
            override fun onResponse(
                call: Call<ArrayList<RucResponse>>,
                response: Response<ArrayList<RucResponse>>
            ) {
                listRuc.value = response.body()
            }

            override fun onFailure(call: Call<ArrayList<RucResponse>>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }

        })

        return listRuc
    }
}