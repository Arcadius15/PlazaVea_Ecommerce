package edu.pe.idat.pva.models

data class UsuarioRequest(
    val cliente: Cliente,
    val email: String,
    val password: String,
    val roles: List<Rol>
)