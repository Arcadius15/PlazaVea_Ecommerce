package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.ResponseHttp
import edu.pe.idat.pva.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserRoutes {
    @POST("users/registrar")
    fun registrar(@Body user: User): Call<ResponseHttp>

    @FormUrlEncoded
    @POST("users/login")
    fun login(@Field("email") email: String, @Field("password") password: String): Call<ResponseHttp>
}