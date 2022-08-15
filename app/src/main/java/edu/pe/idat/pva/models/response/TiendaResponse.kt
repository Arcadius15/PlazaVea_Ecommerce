package edu.pe.idat.pva.models.response

import java.io.Serializable

data class TiendaResponse(
    var direccion: String,
    var horarioA: String,
    var horarioC: String,
    var idTienda: String,
    var lat: Double,
    var lng: Double,
    var nombre: String,
    var numeroTelefonico: String
): Serializable