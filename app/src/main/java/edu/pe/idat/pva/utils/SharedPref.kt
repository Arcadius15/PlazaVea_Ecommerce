package edu.pe.idat.pva.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import java.lang.Exception

class SharedPref(activity: Activity) {
    private var prefs: SharedPreferences? = null

    init{
        prefs = activity.getSharedPreferences("edu.pe.idat.pva", Context.MODE_PRIVATE)
    }

    //metodo para guardar informacion en sesion
    fun save( key: String, objeto: Any){
        try {
            val gson = Gson()
            val json = gson.toJson(objeto)
            with(prefs?.edit()){
                this?.putString(key, json)
                this?.commit()
            }
        }catch (e: Exception){
            Log.d("Error", "Err: ${e.message}")
        }
    }

    //obtener data
    fun getData(key: String): String?{
        val data = prefs?.getString(key, "")
        return data
    }

    fun remove(key: String){
        prefs?.edit()?.remove(key)?.apply()
    }

    fun setSomeBooleanValue(nombre: String, valor: Boolean) {
        val edit = prefs?.edit()
        edit?.putBoolean(nombre, valor)
        edit?.apply()
    }

    fun getSomeBooleanValue(nombre: String) : Boolean {
        return prefs?.getBoolean(nombre,false) ?: false
    }
}