package edu.pe.idat.pva.models

data class Cliente(
    val apellidos: String,
    val dni: Int,
    val fechaNacimiento: String,
    val nombre: String,
    val numTelefonico: String
)