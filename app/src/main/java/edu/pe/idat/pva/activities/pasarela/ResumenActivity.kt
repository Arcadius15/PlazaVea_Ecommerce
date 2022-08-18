package edu.pe.idat.pva.activities.pasarela

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.DireccionRegistroActivity
import edu.pe.idat.pva.activities.RegRucActivity
import edu.pe.idat.pva.activities.RegTarjetaActivity
import edu.pe.idat.pva.databinding.ActivityResumenBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.db.entity.UsuarioEntity
import edu.pe.idat.pva.models.request.*
import edu.pe.idat.pva.models.response.Producto
import edu.pe.idat.pva.models.response.ResponseHttp
import edu.pe.idat.pva.providers.ClienteProvider
import edu.pe.idat.pva.providers.OrdenProvider
import edu.pe.idat.pva.providers.TokenRoomProvider
import edu.pe.idat.pva.providers.UsuarioRoomProvider
import edu.pe.idat.pva.utils.SharedPref
import java.text.SimpleDateFormat
import java.util.*

class ResumenActivity : AppCompatActivity(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private lateinit var binding: ActivityResumenBinding
    private lateinit var sharedPref: SharedPref
    private lateinit var ordenProvider: OrdenProvider
    private lateinit var clienteProvider: ClienteProvider
    private lateinit var usuarioRoomProvider: UsuarioRoomProvider
    private lateinit var tokenRoomProvider: TokenRoomProvider

    private lateinit var usuario: UsuarioEntity
    private lateinit var token: TokenEntity

    private var gson = Gson()

    private var monto = 0.0
    private var igv = 0.0
    private var total = 0.0

    private var tipoFop = 0

    private var ordenIdGenerada = ""
    private var idTienda = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResumenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llpasarela.visibility = View.GONE

        sharedPref = SharedPref(this)

        ordenProvider = ViewModelProvider(this)[OrdenProvider::class.java]
        clienteProvider = ViewModelProvider(this)[ClienteProvider::class.java]
        usuarioRoomProvider = ViewModelProvider(this)[UsuarioRoomProvider::class.java]
        tokenRoomProvider = ViewModelProvider(this)[TokenRoomProvider::class.java]

        binding.llRuc.visibility = View.GONE

        getUserFromDB("c")

        binding.tipogroup.setOnCheckedChangeListener(this)

        val fechaEstimada = Calendar.getInstance()
        fechaEstimada.add(Calendar.DATE, 10)
        val df = SimpleDateFormat("yyyy-MM-dd")
        df.timeZone = TimeZone.getTimeZone("America/Lima")

        binding.tvFechaEstimada.text = "Fecha Estimada de Entrega: ${df.format(fechaEstimada.time)}"

        val sb = StringBuilder()

        getProductsFromSharedPref().forEach{
            monto += it.precioRegular * it.quantity!!

            sb.append("${it.nombre} x ${it.quantity} ..... S/${String.format("%.2f",it.precioRegular * it.quantity!!)}\n")
        }

        igv += monto * 0.18
        total += monto + igv

        binding.tvPrecioPagar.text = "Monto: S/${String.format("%.2f",monto)}" +
                                    "\nIGV (18%): S/${String.format("%.2f",igv)}" +
                                    "\nTotal: S/${String.format("%.2f",total)}"

        binding.tvProductoslista.text = sb.toString()

        binding.progressbarPasarela.visibility = View.GONE
        binding.llpasarela.visibility = View.VISIBLE

        binding.btnconfcompra.setOnClickListener(this)
        binding.btnGoBackResumen.setOnClickListener(this)
        binding.btnregnuevruc.setOnClickListener(this)
        binding.btnregnuevdir.setOnClickListener(this)
        binding.btnregnuevtar.setOnClickListener(this)
        binding.btnselectienda.setOnClickListener(this)

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
            println("Orden ID: $ordenIdGenerada")
            val i = Intent(this,TicketActivity::class.java)
            i.putExtra("ordenId",ordenIdGenerada)
            startActivity(i)
        } else {
            Toast.makeText(this,
                "ERROR! Hubo un problema con el servicio, intente de nuevo más tarde.",
                Toast.LENGTH_LONG).show()
        }
        binding.btnconfcompra.isEnabled = true
        binding.btnGoBackResumen.isEnabled = true
        binding.btnregnuevruc.isEnabled = true
        binding.btnregnuevdir.isEnabled = true
        binding.btnregnuevtar.isEnabled = true
    }

    private fun obtenerOrdenRegistrada(ordenId: String) {
        if (ordenId != "error") {
            ordenIdGenerada = ordenId

            println("Orden ID Antes: $ordenIdGenerada")

            val ordenIdRequest = OrdenIDRequest(ordenId)

            val fechaActual = Calendar.getInstance()
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            df.timeZone = TimeZone.getTimeZone("America/Lima")

            val ordenHistorialRequest = OrdenHistorialRequest(
                "Su pedido está en espera de ser aceptado por un repartidor",
                1,
                df.format(fechaActual.time),
                ordenIdRequest
            )

            ordenProvider.registrarHistorial(ordenHistorialRequest,
                                            "Bearer ${token.token}")
        } else {
            Toast.makeText(this,
                "ERROR! Hubo un problema con el servicio, intente de nuevo más tarde.",
                Toast.LENGTH_LONG).show()
        }
    }

    private fun confirmarCompra() {
        binding.btnconfcompra.isEnabled = false
        binding.btnGoBackResumen.isEnabled = false
        binding.btnregnuevruc.isEnabled = false
        binding.btnregnuevdir.isEnabled = false
        binding.btnregnuevtar.isEnabled = false
        try{
            if (validarCampos()) {
                AlertDialog.Builder(this)
                    .setTitle("Confirmar Compra")
                    .setMessage("¿Seguro que desea confirmar la compra?")
                    .setPositiveButton("Sí") { dialogInterface, _ ->
                        procesarOrden()
                        dialogInterface.cancel()
                    }
                    .setNegativeButton("No"){ dialogInterface, _ ->
                        binding.btnconfcompra.isEnabled = true
                        binding.btnGoBackResumen.isEnabled = true
                        binding.btnregnuevruc.isEnabled = true
                        binding.btnregnuevdir.isEnabled = true
                        binding.btnregnuevtar.isEnabled = true
                        dialogInterface.cancel()
                    }
                    .show()
            } else {
                binding.btnconfcompra.isEnabled = true
                binding.btnGoBackResumen.isEnabled = true
                binding.btnregnuevruc.isEnabled = true
                binding.btnregnuevdir.isEnabled = true
                binding.btnregnuevtar.isEnabled = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            binding.btnconfcompra.isEnabled = true
            binding.btnGoBackResumen.isEnabled = true
            binding.btnregnuevruc.isEnabled = true
            binding.btnregnuevdir.isEnabled = true
            binding.btnregnuevtar.isEnabled = true
        }
    }

    private fun procesarOrden() {
        val clienteIDRequest = ClienteIDRequest(usuario.idCliente)
        println(idTienda)
        val tiendaIDRequest = TiendaIDRequest(idTienda)

        val listOrdenDetalle = ArrayList<OrdendetalleRequest>()

        getProductsFromSharedPref().forEach {
            val productoIDRequest = ProductoIDRequest(it.idProducto)

            val ordendetalleRequest = OrdendetalleRequest(
                it.quantity!!,
                it.precioRegular,
                productoIDRequest
            )

            listOrdenDetalle.add(ordendetalleRequest)
        }

        val fechaActual = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        df.timeZone = TimeZone.getTimeZone("America/Lima")

        var formaPago = ""

        when (tipoFop) {
            1 -> formaPago = binding.edtTarjetaSelect.text.toString()
            2 -> formaPago = binding.edtTarjetaSelect.text.toString() + " " +
                        binding.edtRucSelect.text.toString()
        }

        val ordenRequest = OrdenRequest(
            clienteIDRequest,
            binding.edtDireccionSelect.text.toString(),
            df.format(fechaActual.time),
            formaPago,
            String.format("%.2f",igv).toDouble(),
            String.format("%.2f",monto).toDouble(),
            listOrdenDetalle,
            "ENCAMINO",
            tiendaIDRequest,
            tipoFop,
            String.format("%.2f",total).toDouble()
        )

        ordenProvider.registrarOrden(ordenRequest,
            "Bearer ${token.token}")
    }

    private fun cargarListas(){
        clienteProvider.listarRuc(usuario.idCliente, "Bearer " + token.token).observe(this){
            val rucs = ArrayList<String>()

            if (it != null) {
                it.forEach { rr ->
                    rucs.add(rr.numeroRuc)
                }
            } else {
                rucs.add("No tiene ningún RUC")
            }

            val adapterRuc = ArrayAdapter(
                this,
                R.layout.drop_down_item,
                rucs
            )

            binding.edtRucSelect.setAdapter(adapterRuc)
        }

        clienteProvider.listarDirecciones(usuario.idCliente, "Bearer " + token.token).observe(this){
            val direcciones = ArrayList<String>()

            if (it != null) {
                it.forEach { dr ->
                    direcciones.add(dr.direccion)
                }
            } else {
                direcciones.add("No tiene ninguna dirección")
            }

            val adapterDir = ArrayAdapter(
                this,
                R.layout.drop_down_item,
                direcciones
            )

            binding.edtDireccionSelect.setAdapter(adapterDir)
        }

        clienteProvider.listarTarjetas(usuario.idCliente, "Bearer " + token.token).observe(this){
            val tarjetas = ArrayList<String>()

            if (it != null) {
                it.forEach { tr ->
                    tarjetas.add(tr.numeroTarjeta)
                }
            } else {
                tarjetas.add("No tiene ninguna tarjeta")
            }

            val adapterTar = ArrayAdapter(
                this,
                R.layout.drop_down_item,
                tarjetas
            )

            binding.edtTarjetaSelect.setAdapter(adapterTar)
        }
    }

    private fun validarCampos() : Boolean {
        if (tipoFop == 0) {
            Toast.makeText(this,
                "Seleccione un tipo de ticket",
                Toast.LENGTH_LONG).show()
            return false
        }

        if (tipoFop == 2) {
            if (binding.edtRucSelect.text.toString().isNullOrBlank() ||
                binding.edtRucSelect.text.toString() == "No tiene ningún RUC") {
                Toast.makeText(this,
                    "Seleccione o registre un RUC",
                    Toast.LENGTH_LONG).show()
                return false
            }
        }

        if (binding.edtDireccionSelect.text.toString().isNullOrBlank() ||
            binding.edtDireccionSelect.text.toString() == "No tiene ninguna dirección") {
            Toast.makeText(this,
                "Seleccione o registre una dirección",
                Toast.LENGTH_LONG).show()
            return false
        }

        if (binding.edtTarjetaSelect.text.toString().isNullOrBlank() ||
            binding.edtTarjetaSelect.text.toString() == "No tiene ninguna tarjeta") {
            Toast.makeText(this,
                "Seleccione o registre una tarjeta",
                Toast.LENGTH_LONG).show()
            return false
        }

        if (idTienda == "0"){
            Toast.makeText(this,
                "Seleccione una tienda",
                Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    private fun getProductsFromSharedPref() : ArrayList<Producto> {
        val selectProduct: ArrayList<Producto>
        val type = object : TypeToken<ArrayList<Producto>>() {}.type
        selectProduct = gson.fromJson(sharedPref.getData("shopBag"), type)
        return selectProduct
    }

    private fun getUserFromDB(origen: String){
        usuarioRoomProvider.obtener().observe(this){
            usuario = it
            getTokenFromDB(origen)
        }
    }

    private fun getTokenFromDB(origen: String){
        tokenRoomProvider.obtener().observe(this){
            token = it
            when (origen) {
                "c" -> cargarListas()
                "con" -> confirmarCompra()
            }
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnconfcompra -> getUserFromDB("con")
            R.id.btnGoBackResumen -> finish()
            R.id.btnregnuevruc -> launcher.launch(Intent(this,RegRucActivity::class.java))
            R.id.btnregnuevdir -> launcher.launch(Intent(this,DireccionRegistroActivity::class.java))
            R.id.btnregnuevtar -> launcher.launch(Intent(this,RegTarjetaActivity::class.java))
            R.id.btnselectienda -> launcher.launch(Intent(this,ListarTiendasActivity::class.java))
        }
    }

    override fun onCheckedChanged(p0: RadioGroup, checkedId: Int) {
        when (checkedId) {
            R.id.rbBoleta -> {
                tipoFop = 1
                binding.llRuc.visibility = View.GONE
            }
            R.id.rbFactura -> {
                tipoFop = 2
                binding.llRuc.visibility = View.VISIBLE
            }
        }
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data?.getStringExtra("nombreTienda") != null) {
                binding.tvTienda.text = it.data!!.getStringExtra("nombreTienda")
                idTienda = it.data!!.getStringExtra("idTienda")!!
            }
            binding.llpasarela.visibility = View.GONE
            binding.progressbarPasarela.visibility = View.VISIBLE
            cargarListas()
            binding.progressbarPasarela.visibility = View.GONE
            binding.llpasarela.visibility = View.VISIBLE
        }
    }
}