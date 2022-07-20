package edu.pe.idat.pva.activities.pasarela

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.HomeActivity
import edu.pe.idat.pva.databinding.ActivityTicketBinding
import edu.pe.idat.pva.databinding.CardviewShoppingbagBinding
import edu.pe.idat.pva.models.LoginResponse
import edu.pe.idat.pva.models.OrdenResponse
import edu.pe.idat.pva.models.Producto
import edu.pe.idat.pva.models.UsuarioResponse
import edu.pe.idat.pva.providers.OrdenProvider
import edu.pe.idat.pva.utils.SharedPref
import java.lang.StringBuilder

class TicketActivity : AppCompatActivity(){

    private lateinit var binding: ActivityTicketBinding
    private lateinit var sharedPref: SharedPref
    private lateinit var ordenProvider: OrdenProvider

    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)
        binding.btnFinPasarela.setOnClickListener{gotoHome()}
        ordenProvider = ViewModelProvider(this)[OrdenProvider::class.java]

        cargarDatos()
    }

    private fun cargarDatos() {
        println("Orden ID XD: " + intent.getStringExtra("ordenId").toString())
        ordenProvider.getOrden(intent.getStringExtra("ordenId").toString(),
                                "Bearer ${getTokenFromSession()!!.token}").observe(this){
                                    obtenerOrden(it!!)
        }
    }

    private fun obtenerOrden(ordenResponse: OrdenResponse) {
        if (ordenResponse.tipoFop == 2) {
            binding.tvTicketTipo.text = "Factura"
        } else {
            binding.tvTicketTipo.text = "Boleta"
        }

        binding.tvTicketMetodo.text = "Tarjeta: ****${ordenResponse.formaPago.takeLast(4)}"
        binding.tvTicketFecha.text = ordenResponse.fecha

        val sb = StringBuilder()

        ordenResponse.ordendetalle.forEach {
            sb.append("${it.producto.idProducto} x ${it.cantidad} ..... ${it.precio * it.cantidad}\n")
        }

        binding.tvProductoslista.text = sb.toString()

        binding.tvTicketMonto.text = "S/" + ordenResponse.monto.toString()
        binding.tvTicketIGV.text = "S/" + ordenResponse.igv.toString()
        binding.tvTicketTotal.text = "S/" + ordenResponse.total.toString()
    }

    private fun gotoHome(){
        val i = Intent(this, HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getUserFromSession(): UsuarioResponse?{
        val gson = Gson()

        return if(sharedPref.getData("user").isNullOrBlank()){
            null
        } else {
            val user = gson.fromJson(sharedPref.getData("user"), UsuarioResponse::class.java)
            user
        }
    }

    private fun getTokenFromSession(): LoginResponse?{
        val gson = Gson()

        return if(sharedPref.getData("token").isNullOrBlank()){
            null
        } else {
            val token = gson.fromJson(sharedPref.getData("token"), LoginResponse::class.java)
            token
        }
    }
}