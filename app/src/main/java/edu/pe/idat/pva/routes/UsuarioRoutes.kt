package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.*
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