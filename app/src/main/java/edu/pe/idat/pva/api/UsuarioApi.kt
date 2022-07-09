package edu.pe.idat.pva.api

import edu.pe.idat.pva.models.UsuarioRequest
import edu.pe.idat.pva.models.UsuarioResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioApi {

    @POST("jwt/registro")
    fun registrar(@Body usuarioRequest: UsuarioRequest) : Call<UsuarioResponse>

}