package edu.pe.idat.pva.models

import java.io.Serializable

data class RucResponse(
    val idRuc: Int,
    val numeroRuc: String
) : Serializable