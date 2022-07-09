package edu.pe.idat.pva.models

data class ClienteResponse(
    val apellidos: String,
    val direcciones: List<Direccion>,
    val dni: String,
    val fechaNacimiento: String,
    val idCliente: String,
    val nombre: String,
    val numTelefonico: String,
    val ordenes: List<Orden>,
    val rucs: List<Ruc>,
    val tarjetas: List<Tarjeta>
)
