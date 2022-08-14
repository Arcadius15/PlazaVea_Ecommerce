package edu.pe.idat.pva.models.request

data class DireccionPatchRequest(
    var direccion: String,
    var latitud: Double,
    var longitud: Double
)