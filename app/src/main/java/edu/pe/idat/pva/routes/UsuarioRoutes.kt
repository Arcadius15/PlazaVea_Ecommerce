package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.request.LoginRequest
import edu.pe.idat.pva.models.request.UsuarioPswRequest
import edu.pe.idat.pva.models.request.UsuarioRequest
import edu.pe.idat.pva.models.response.LoginResponse
import edu.pe.idat.pva.models.response.Mensaje
import edu.pe.idat.pva.models.response.UsuarioResponse
import retrofit2.Call
import retrofit2.http.*

interface UsuarioRoutes {

    @POST("jwt/registro")
    fun registrar(@Body usuarioRequest: UsuarioRequest) : Call<UsuarioResponse>

    @POST("jwt/authenticate")
    fun autenticar(@Body loginRequest: LoginRequest) : Call<LoginResponse>

    @POST("jwt/getuserdetails")
    fun getUserByEmail(@Body loginRequest: LoginRequest,
                       @Header("Authorization") token: String) : Call<UsuarioResponse>

    @PUT("jwt/editpassword")
    fun editPassword(@Body usuarioPswRequest: UsuarioPswRequest) : Call<Mensaje>
}