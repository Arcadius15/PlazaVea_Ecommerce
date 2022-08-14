package edu.pe.idat.pva.models.response

data class ProductosCategoriaResponse(
    val content: List<Producto>,
    var totalPages: Int
)
