package edu.pe.idat.pva.models.request

data class TarjetaRequest(
    val cvv: String,
    val fechaCaducidad: String,
    val nombrePropietario: String,
    val numeroTarjeta: String,
    val tipo: Int,
    val cliente: ClienteIDRequest
)