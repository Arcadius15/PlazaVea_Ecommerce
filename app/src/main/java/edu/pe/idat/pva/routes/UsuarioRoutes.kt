package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.LoginRequest
import edu.pe.idat.pva.models.LoginResponse
import edu.pe.idat.pva.models.UsuarioRequest
import edu.pe.idat.pva.models.UsuarioResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface UsuarioRoutes {

    @POST("jwt/registro")
    fun registrar(@Body usuarioRequest: UsuarioRequest) : Call<UsuarioResponse>

    @POST("jwt/authenticate")
    fun autenticar(@Body loginRequest: LoginRequest) : Call<LoginResponse>

    @GET("/")
    fun getUserByEmail(@Field("email") email: String) : Call<UsuarioResponse>
}