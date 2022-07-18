package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.OrdenHistorialRequest
import edu.pe.idat.pva.models.OrdenRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OrdenRoutes {

    @POST("orden")
    fun registrarOrden(
        @Body ordenRequest: OrdenRequest,
        @Header("Authorization") token: String
    ): Call<String>

    @POST("historial")
    fun registrarHistorial(
        @Body ordenHistorialRequest: OrdenHistorialRequest,
        @Header("Authorization") token: String
    ): Call<Void>
}