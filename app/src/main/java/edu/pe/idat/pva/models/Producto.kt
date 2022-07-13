package edu.pe.idat.pva.models

data class Producto(
    val descripciones: List<Descripcione>,
    val imagenUrl: String,
    val nombre: String,
    val oferta: Boolean,
    val precioOferta: Int,
    val precioRegular: Int
)