package edu.pe.idat.pva.models.response

import java.io.Serializable

data class OrdenDetalleResponse(
    var cantidad: Int,
    var precio: Double,
    var producto: ProductoOrdenResponse
) : Serializable