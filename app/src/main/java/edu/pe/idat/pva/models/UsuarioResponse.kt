package edu.pe.idat.pva.models

data class UsuarioResponse(
    val activo: Boolean,
    val blocked: Boolean,
    val cliente: ClienteResponse,
    val email: String,
    val idUsuario: String,
    val pswExp: String,
    val roles: List<Role>
)
