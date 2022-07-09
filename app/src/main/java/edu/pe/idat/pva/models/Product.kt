package edu.pe.idat.pva.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Product (
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("image1") val image1: String? = null,
    @SerializedName("price") val price: Double,
    @SerializedName("id_category") val idCategoria: String,
    @SerializedName("quantity") var quantity: Int? = null
    ){
    fun toJson(): String{
        return Gson().toJson(this)
    }

    override fun toString(): String {
        return "Product(id='$id', name='$name', description='$description', image1='$image1', price=$price, idCategoria='$idCategoria', quantity=$quantity)"
    }


}