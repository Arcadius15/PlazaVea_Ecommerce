package edu.pe.idat.pva.apirep

import android.util.Log
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.pva.api.RetrofitInstanceCreate
import edu.pe.idat.pva.models.response.ResponseHttp
import edu.pe.idat.pva.models.request.RucPatchRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RucApiRepository {

    var responseHttp = MutableLiveData<ResponseHttp>()

    fun editarRuc(idRuc: Int, rucPatchRequest: RucPatchRequest, token: String) : MutableLiveData<ResponseHttp> {
        val call: Call<Void> = RetrofitInstanceCreate
            .getRucRoutes.editarRuc(idRuc, rucPatchRequest, token)
        call.enqueue(object : Callback<Void> {
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

    fun borrarRuc(idRuc: Int, token: String) : MutableLiveData<ResponseHttp> {
        val call: Call<Void> = RetrofitInstanceCreate
            .getRucRoutes.borrarRuc(idRuc, token)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 204){
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