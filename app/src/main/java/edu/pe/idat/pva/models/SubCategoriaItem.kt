package edu.pe.idat.pva.models

data class SubCategoriaItem(
    val categoria: Categoria,
    val idSubcategoria: Int,
    val nombre: String,
    val tipos: List<Tipo>,
    val urlFoto: Any
)