package edu.pe.idat.pva.models.request

data class UsuarioRequest(
    val cliente: Cliente,
    val email: String,
    val password: String,
    val roles: List<String>

)
