package edu.pe.idat.pva.models

import java.io.Serializable

data class TarjetaResponse(
    var idTarjeta: Int,
    var fechaCaducidad: String,
    var nombrePropietario: String,
    var numeroTarjeta: String,
    var tipo: Int
) : Serializable