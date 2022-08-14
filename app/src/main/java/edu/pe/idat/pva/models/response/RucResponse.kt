package edu.pe.idat.pva.models.response

import java.io.Serializable

data class RucResponse(
    val idRuc: Int,
    val numeroRuc: String
) : Serializable