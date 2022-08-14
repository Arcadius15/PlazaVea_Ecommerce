package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.request.TarjetaPatchRequest
import retrofit2.Call
import retrofit2.http.*

interface TarjetaRoutes {

    @PATCH("tarjeta/{idTarjeta}")
    fun editarTarjeta(@Path("idTarjeta") idTarjeta: Int,
                      @Body tarjetaPatchRequest: TarjetaPatchRequest,
                      @Header("Authorization") token: String) : Call<Void>

    @DELETE("tarjeta/{idTarjeta}")
    fun borrarTarjeta(@Path("idTarjeta") idTarjeta: Int,
                      @Header("Authorization") token: String) : Call<Void>

}