package edu.pe.idat.pva.api

import edu.pe.idat.pva.models.UsuarioRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioApi {

    @POST("registro")
    fun registrar(@Body usuarioRequest: UsuarioRequest) : Call<UsuarioRequest>

}