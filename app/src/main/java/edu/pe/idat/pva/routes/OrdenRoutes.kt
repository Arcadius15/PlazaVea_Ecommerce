package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.OrdenHistorialRequest
import edu.pe.idat.pva.models.OrdenPageResponse
import edu.pe.idat.pva.models.OrdenRequest
import edu.pe.idat.pva.models.OrdenResponse
import edu.pe.idat.pva.models.request.OrdenPatchRequest
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
                        @Header("Authorization") token: String) : Call<OrdenPageResponse>

    @PATCH("orden/{idOrden}")
    fun actualizarOrden(@Path("idOrden") idOrden: String,
                        @Body ordenPatchRequest: OrdenPatchRequest,
                        @Header("Authorization") token: String): Call<Void>
}