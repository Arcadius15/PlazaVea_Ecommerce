package edu.pe.idat.pva.models.request

data class DireccionRequest(
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    val cliente: ClienteIDRequest
)