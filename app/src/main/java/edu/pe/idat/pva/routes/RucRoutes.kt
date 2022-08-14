package edu.pe.idat.pva.routes

import edu.pe.idat.pva.models.request.RucPatchRequest
import retrofit2.Call
import retrofit2.http.*

interface RucRoutes {

    @PATCH("ruc/{idRuc}")
    fun editarRuc(@Path("idRuc") idRuc: Int,
                  @Body rucPatchRequest: RucPatchRequest,
                  @Header("Authorization") token: String) : Call<Void>

    @DELETE("ruc/{idRuc}")
    fun borrarRuc(@Path("idRuc") idRuc: Int,
                  @Header("Authorization") token: String) : Call<Void>

}