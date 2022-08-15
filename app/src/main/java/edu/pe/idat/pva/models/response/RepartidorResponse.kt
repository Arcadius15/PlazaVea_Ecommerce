package edu.pe.idat.pva.models.response

data class RepartidorResponse(
    var apellidos: String,
    var direccion: String,
    var dni: String,
    var fechaNacimiento: String,
    var idRepartidor: String,
    var nombre: String,
    var numTelefonico: String,
    var placa: String,
    var status: String,
    var turno: Int
)