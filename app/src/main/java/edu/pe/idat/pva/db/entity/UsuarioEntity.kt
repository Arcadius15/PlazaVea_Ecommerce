package edu.pe.idat.pva.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class UsuarioEntity(
    @PrimaryKey
    var idUsuario: String,
    var email: String,
    var activo: Boolean,
    var blocked: Boolean,
    var idCliente: String,
    var nombre: String,
    var apellidos: String,
    var dni: String,
    var numTelefonico: String
)
