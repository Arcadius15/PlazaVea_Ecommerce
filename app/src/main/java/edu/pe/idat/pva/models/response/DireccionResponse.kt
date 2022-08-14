package edu.pe.idat.pva.models.response

import java.io.Serializable

data class DireccionResponse(
    val idDireccion : Int,
    val direccion: String,
    val latitud: Double,
    val longitud: Double
) : Serializable
