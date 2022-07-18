package edu.pe.idat.pva.models

data class OrdendetalleRequest(
    var cantidad: Int,
    var precio: Double,
    var producto: ProductoIDRequest
)