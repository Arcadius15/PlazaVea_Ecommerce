package edu.pe.idat.pva.apirep

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import edu.pe.idat.pva.api.ApiRoutes
import edu.pe.idat.pva.api.RetrofitInstanceCreate
import edu.pe.idat.pva.models.UsuarioRequest
import edu.pe.idat.pva.models.UsuarioResponse
import edu.pe.idat.pva.providers.UsuarioProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsuarioApiRepository {
    var usuarioResponse = MutableLiveData<UsuarioResponse>()

    fun registrar(usuarioRequest: UsuarioRequest) : MutableLiveData<UsuarioResponse>{
        val call: Call<UsuarioResponse> = RetrofitInstanceCreate
            .getUsuarioRoutes.registrar(usuarioRequest)
        call.enqueue(object : Callback<UsuarioResponse>{
            override fun onResponse(call: Call<UsuarioResponse>, response: Response<UsuarioResponse>) {
                usuarioResponse.value = response.body()
            }

            override fun onFailure(call: Call<UsuarioResponse>, t: Throwable) {
                Log.e("ERROR!", t.message.toString())
            }

        })

        return usuarioResponse
    }
}