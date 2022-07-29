package edu.pe.idat.pva.models

import java.io.Serializable

data class DireccionResponse(
    val idDireccion : Int,
    val direccion: String,
    val latitud: Double,
    val longitud: Double
) : Serializable
