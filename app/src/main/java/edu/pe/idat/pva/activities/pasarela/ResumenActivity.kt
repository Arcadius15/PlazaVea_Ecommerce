package edu.pe.idat.pva.activities.pasarela

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.RegisterActivity
import edu.pe.idat.pva.adapter.ShoppingBagAdapter
import edu.pe.idat.pva.databinding.ActivityResumenBinding
import edu.pe.idat.pva.models.*
import edu.pe.idat.pva.providers.OrdenProvider
import edu.pe.idat.pva.utils.SharedPref
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ResumenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResumenBinding
    private lateinit var sharedPref: SharedPref
    private lateinit var ordenProvider: OrdenProvider

    var gson = Gson()

    var monto = 0.0
    var igv = 0.0
    var total = 0.0

    var ordenIdGenerada = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)

        ordenProvider = ViewModelProvider(this)[OrdenProvider::class.java]

        if (intent.getStringExtra("tipo").equals("Factura")) {
            binding.tvTipoTicket.text = intent.getStringExtra("tipo").toString() +
                                        "\nRUC: ${intent.getStringExtra("ruc").toString()}"
        } else {
            binding.tvTipoTicket.text = intent.getStringExtra("tipo").toString()
        }

        var fechaEstimada = LocalDate.now().plusDays(10)
        val df = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        binding.tvDireccion.text = intent.getStringExtra("direccion").toString() +
                                    "\nFecha Estimada de Entrega: ${fechaEstimada.format(df)}"

        binding.tvMetodoPago.text = "Tarjeta: ****${intent.getStringExtra("numtarjeta")!!.takeLast(4)}"

        val sb = StringBuilder()

        getProductsFromSharedPref().forEach{
            monto += it.precioRegular * it.quantity!!
            igv += monto * 0.18
            total += monto + igv

            sb.append("${it.nombre} x ${it.quantity} ..... S/${String.format("%.2f",it.precioRegular * it.quantity!!)}\n")
        }

        binding.tvPrecioPagar.text = "Monto: S/${String.format("%.2f",monto)}" +
                                    "\nIGV (18%): S/${String.format("%.2f",igv)}" +
                                    "\nTotal: S/${String.format("%.2f",total)}"

        binding.tvProductoslista.text = sb.toString()

        binding.btnconfcompra.setOnClickListener{ confirmarCompra() }
        binding.btnGoBackResumen.setOnClickListener{ finish() }

        ordenProvider.ordenId.observe(this){
            try {
                obtenerOrdenRegistrada(it!!)
            } catch (e: Exception) {
                Toast.makeText(this,
                    "ERROR! Hubo un problema con el servicio, intente de nuevo más tarde.",
                    Toast.LENGTH_LONG).show()
                binding.btnconfcompra.isEnabled = true
            }
        }

        ordenProvider.responseHttp.observe(this){
            obtenerRespuesta(it!!)
        }
    }

    private fun obtenerRespuesta(responseHttp: ResponseHttp) {
        if (responseHttp.isSuccess){
            Toast.makeText(this,
                "Orden registrada",
                Toast.LENGTH_LONG).show()
            sharedPref.remove("shopBag")
            println("Orden ID: "+ordenIdGenerada)
            val i = Intent(this,TicketActivity::class.java)
            i.putExtra("ordenId",ordenIdGenerada)
            startActivity(i)
        } else {
            Toast.makeText(this,
                "ERROR! Hubo un problema con el servicio, intente de nuevo más tarde.",
                Toast.LENGTH_LONG).show()
        }
        binding.btnconfcompra.isEnabled = true
    }

    private fun obtenerOrdenRegistrada(ordenId: String) {
        if (ordenId != "error") {
            ordenIdGenerada = ordenId

            val ordenIdRequest = OrdenIDRequest(ordenId)

            var fechaActual = Calendar.getInstance()
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            val ordenHistorialRequest = OrdenHistorialRequest(
                "Su pedido está en espera de ser aceptado por un repartidor",
                1,
                df.format(fechaActual.time),
                ordenIdRequest
            )

            ordenProvider.registrarHistorial(ordenHistorialRequest,
                                            "Bearer ${getTokenFromSession()!!.token}")
        } else {
            Toast.makeText(this,
                "ERROR! Hubo un problema con el servicio, intente de nuevo más tarde.",
                Toast.LENGTH_LONG).show()
        }
    }

    private fun confirmarCompra() {
        binding.btnconfcompra.isEnabled = false
        try{
            AlertDialog.Builder(this)
                .setTitle("Confirmar Compra")
                .setMessage("¿Seguro que desea confirmar la compra?")
                .setPositiveButton("Sí") { dialogInterface, i ->
                    procesarOrden()
                    dialogInterface.cancel()
                }
                .setNegativeButton("No"){ dialogInterface, i ->
                    binding.btnconfcompra.isEnabled = true
                    dialogInterface.cancel()
                }
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
            binding.btnconfcompra.isEnabled = true
        }
    }

    private fun procesarOrden() {
        val clienteIDRequest = ClienteIDRequest(getUserFromSession()!!.cliente.idCliente)
        val tiendaIDRequest = TiendaIDRequest("T_00001")

        var listOrdenDetalle = ArrayList<OrdendetalleRequest>()

        getProductsFromSharedPref().forEach {
            val productoIDRequest = ProductoIDRequest(it.idProducto)

            val ordendetalleRequest = OrdendetalleRequest(
                it.quantity!!,
                it.precioRegular,
                productoIDRequest
            )

            listOrdenDetalle.add(ordendetalleRequest)
        }

        var fechaActual = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        var tipoFop: Int
        if (intent.getStringExtra("tipo").equals("Factura")) {
            tipoFop = 2
        } else {
            tipoFop = 1
        }

        val ordenRequest = OrdenRequest(
            clienteIDRequest,
            intent.getStringExtra("direccion").toString(),
            df.format(fechaActual.time),
            intent.getStringExtra("numtarjeta").toString(),
            String.format("%.2f",igv).toDouble(),
            String.format("%.2f",monto).toDouble(),
            listOrdenDetalle,
            "ENCAMINO",
            tiendaIDRequest,
            tipoFop,
            String.format("%.2f",total).toDouble()
        )

        ordenProvider.registrarOrden(ordenRequest,
                                    "Bearer ${getTokenFromSession()!!.token}")
    }

    private fun getProductsFromSharedPref() : ArrayList<Producto> {
        var selectProduct: ArrayList<Producto>
        val type = object : TypeToken<ArrayList<Producto>>() {}.type
        selectProduct = gson.fromJson(sharedPref.getData("shopBag"), type)
        return selectProduct
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