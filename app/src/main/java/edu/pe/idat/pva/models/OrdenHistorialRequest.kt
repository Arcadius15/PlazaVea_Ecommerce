package edu.pe.idat.pva.models

data class OrdenHistorialRequest(
    var descripcion: String,
    var estado: Int,
    var fechaEstado: String,
    var orden: OrdenIDRequest
)