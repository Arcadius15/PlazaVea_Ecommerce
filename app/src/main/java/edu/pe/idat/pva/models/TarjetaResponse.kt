package edu.pe.idat.pva.models

data class TarjetaResponse(
    var fechaCaducidad: String,
    var nombrePropietario: String,
    var numeroTarjeta: String,
    var tipo: Int
)