package edu.pe.idat.pva.models.response

data class ClienteResponse(
    val apellidos: String,
    val direcciones: MutableList<DireccionResponse>,
    val dni: String,
    val fechaNacimiento: String,
    val idCliente: String,
    val nombre: String,
    val numTelefonico: String

)
