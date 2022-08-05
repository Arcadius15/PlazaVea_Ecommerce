package edu.pe.idat.pva.models

import java.io.Serializable

data class ProductoOrdenResponse(
    var idProducto: String,
    var imagenUrl: String,
    var nombre: String
) : Serializable