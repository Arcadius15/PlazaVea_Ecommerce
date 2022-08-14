package edu.pe.idat.pva.models.request

data class UsuarioPswRequest(
    var email: String,
    var newPassword: String,
    var oldPassword: String
)