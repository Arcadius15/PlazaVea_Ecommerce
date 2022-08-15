package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.request.OrdenHistorialRequest
import edu.pe.idat.pva.models.response.OrdenPageResponse
import edu.pe.idat.pva.models.request.OrdenRequest
import edu.pe.idat.pva.models.response.OrdenResponse
import edu.pe.idat.pva.models.request.OrdenPatchRequest
import edu.pe.idat.pva.models.response.RepartidorResponse
import retrofit2.Call
import retrofit2.http.*

interface OrdenRoutes {

    @POST("orden")
    fun registrarOrden(
        @Body ordenRequest: OrdenRequest,
        @Header("Authorization") token: String
    ): Call<String>

    @POST("historial")
    fun registrarHistorial(
        @Body ordenHistorialRequest: OrdenHistorialRequest,
        @Header("Authorization") token: String): Call<Void>

    @GET("orden/{idOrden}")
    fun getOrden(@Path("idOrden") idOrden: String,
                @Header("Authorization") token: String): Call<OrdenResponse>

    @GET("orden/listar/{idCliente}")
    fun getAllByCliente(@Path("idCliente") idCliente: String,
                        @Query("page") page: Int,
                        @Header("Authorization") token: String) : Call<OrdenPageResponse>

    @PATCH("orden/{idOrden}")
    fun actualizarOrden(@Path("idOrden") idOrden: String,
                        @Body ordenPatchRequest: OrdenPatchRequest,
                        @Header("Authorization") token: String): Call<Void>

    @GET("repartidor/{idRepartidor}")
    fun getRepartidor(@Path("idRepartidor") idRepartidor: String,
                      @Header("Authorization") token: String): Call<RepartidorResponse>
}