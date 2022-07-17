package edu.pe.idat.pva.models

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

class ResponseHttp (
    @SerializedName("message") val message: String,
    @SerializedName("success") var isSuccess: Boolean,
    @SerializedName("data") val data: String,
    @SerializedName("error") val error: String
)
{
    override fun toString(): String {
        return "ResponseHttp(message='$message', isSuccess=$isSuccess, data=$data, error='$error')"
    }
}