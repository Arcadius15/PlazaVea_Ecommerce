package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.request.DireccionPatchRequest
import retrofit2.Call
import retrofit2.http.*

interface DireccionRoutes {

    @DELETE("direccion/{idDireccion}")
    fun borrarDireccion(@Path("idDireccion") idDireccion: Int,
                        @Header("Authorization") token: String) : Call<Void>

    @PATCH("direccion/{idDireccion}")
    fun editarDireccion(@Path("idDireccion") idDireccion: Int,
                        @Body direccionPatchRequest: DireccionPatchRequest,
                        @Header("Authorization") token: String) : Call<Void>
}