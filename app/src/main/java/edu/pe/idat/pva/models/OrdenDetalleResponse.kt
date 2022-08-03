package edu.pe.idat.pva.models

data class OrdenDetalleResponse(
    var cantidad: Int,
    var precio: Double,
    var producto: ProductoOrdenResponse
)