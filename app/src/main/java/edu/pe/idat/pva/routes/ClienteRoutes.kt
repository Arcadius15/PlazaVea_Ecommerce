package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.request.DireccionRequest
import edu.pe.idat.pva.models.request.RucRequest
import edu.pe.idat.pva.models.request.TarjetaRequest
import edu.pe.idat.pva.models.response.DireccionResponse
import edu.pe.idat.pva.models.response.RucResponse
import edu.pe.idat.pva.models.response.TarjetaResponse
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

    @GET("tarjeta/listar/{idCliente}")
    fun listarTarjetas(@Path("idCliente") idCliente: String,
                        @Header("Authorization") token: String) : Call<ArrayList<TarjetaResponse>>

    @GET("direccion/listar/{idCliente}")
    fun listarDirecciones(@Path("idCliente") idCliente: String,
                       @Header("Authorization") token: String) : Call<ArrayList<DireccionResponse>>

    @GET("ruc/listar/{idCliente}")
    fun listarRuc(@Path("idCliente") idCliente: String,
                          @Header("Authorization") token: String) : Call<ArrayList<RucResponse>>
}