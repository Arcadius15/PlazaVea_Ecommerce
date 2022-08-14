package edu.pe.idat.pva.models.request

data class TarjetaPatchRequest(
    var cvv: String,
    var fechaCaducidad: String,
    var nombrePropietario: String,
    var numeroTarjeta: String,
    var tipo: Int
)