package edu.pe.idat.pva.models

data class UsuarioPswRequest(
    var email: String,
    var newPassword: String,
    var oldPassword: String
)