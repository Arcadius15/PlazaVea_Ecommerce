package edu.pe.idat.pva.models

data class DireccionPatchRequest(
    var direccion: String,
    var latitud: Double,
    var longitud: Double
)