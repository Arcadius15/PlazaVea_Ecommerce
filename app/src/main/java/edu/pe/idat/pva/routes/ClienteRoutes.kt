package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.DireccionRequest
import edu.pe.idat.pva.models.RucRequest
import edu.pe.idat.pva.models.TarjetaRequest
import retrofit2.Call
import retrofit2.http.*

interface ClienteRoutes {

    @POST("direccion")
    fun registrarDireccion(@Body direccionRequest: DireccionRequest,
                  @Header("Authorization") token: String) : Call<Void>

    @POST("ruc")
    fun registrarRuc(@Body rucRequest: RucRequest,
                     @Header("Authorization") token: String) : Call<Void>

    @POST("tarjeta")
    fun registrarTarjeta(@Body tarjetaRequest: TarjetaRequest,
                         @Header("Authorization") token: String) : Call<Void>
}