package edu.pe.idat.pva.activities.pasarela

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.pe.idat.pva.R
import edu.pe.idat.pva.adapter.ShoppingBagAdapter
import edu.pe.idat.pva.databinding.ActivityResumenBinding
import edu.pe.idat.pva.models.Producto
import edu.pe.idat.pva.utils.SharedPref
import java.lang.StringBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ResumenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResumenBinding
    private lateinit var sharedPref: SharedPref

    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)

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

        var monto = 0.0
        var igv = 0.0
        var total = 0.0

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
    }

    private fun getProductsFromSharedPref() : ArrayList<Producto> {
        var selectProduct: ArrayList<Producto>
        val type = object : TypeToken<ArrayList<Producto>>() {}.type
        selectProduct = gson.fromJson(sharedPref.getData("shopBag"), type)
        return selectProduct
    }
}