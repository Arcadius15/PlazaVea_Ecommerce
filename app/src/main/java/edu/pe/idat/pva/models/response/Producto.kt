package edu.pe.idat.pva.models.response

import com.google.gson.Gson

data class Producto(
    val idProducto: String,
    val descripciones: List<Descripcione>,
    val imagenUrl: String,
    val nombre: String,
    val oferta: Boolean,
    val precioOferta: Double,
    val precioRegular: Double,
    var quantity: Int? = null
){

    fun toJson(): String{
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Producto(idProducto='$idProducto', descripciones=$descripciones, imagenUrl='$imagenUrl', nombre='$nombre', oferta=$oferta, precioOferta=$precioOferta, precioRegular=$precioRegular, quantity=$quantity)"
    }


}

