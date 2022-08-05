package edu.pe.idat.pva.models

import java.io.Serializable

data class OrdenDetalleResponse(
    var cantidad: Int,
    var precio: Double,
    var producto: ProductoOrdenResponse
) : Serializable