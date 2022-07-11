package edu.pe.idat.pva.models

data class SubCategoriaResponse(
    val idSubcategoria: Int,
    val nombre: String,
    val tipos: List<Tipo>,
    val urlFoto: String
)