package edu.pe.idat.pva.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pe.idat.pva.R
import edu.pe.idat.pva.adapter.ProductosOrdenAdapter
import edu.pe.idat.pva.databinding.ActivityDetalleOrdenBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.models.OrdenResponse
import edu.pe.idat.pva.models.ResponseHttp
import edu.pe.idat.pva.models.request.OrdenPatchRequest
import edu.pe.idat.pva.providers.OrdenProvider
import edu.pe.idat.pva.providers.TokenRoomProvider
import java.text.SimpleDateFormat
import java.util.*

class DetalleOrdenActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetalleOrdenBinding
    private lateinit var ordenResponse: OrdenResponse
    private lateinit var token: TokenEntity

    private lateinit var ordenProvider: OrdenProvider
    private lateinit var tokenRoomProvider: TokenRoomProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleOrdenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordenResponse = intent.getSerializableExtra("orden") as OrdenResponse

        ordenProvider = ViewModelProvider(this)[OrdenProvider::class.java]
        tokenRoomProvider = ViewModelProvider(this)[TokenRoomProvider::class.java]

        binding.rvProductosOrden.setHasFixedSize(true)
        binding.rvProductosOrden.layoutManager = LinearLayoutManager(this)

        cargarDatos()

        binding.btncancelar.setOnClickListener(this)
        binding.btnGoBackOrden.setOnClickListener(this)

        ordenProvider.responseHttp.observe(this){
            obtenerRespuesta(it)
        }
    }

    private fun obtenerRespuesta(responseHttp: ResponseHttp) {
        if (responseHttp.isSuccess) {
            Toast.makeText(
                applicationContext,
                "Compra Cancelada",
                Toast.LENGTH_LONG
            ).show()
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        } else {
            Toast.makeText(
                applicationContext,
                "Hubo un problema con el servicio",
                Toast.LENGTH_LONG
            ).show()
            ordenResponse.status = "ENCAMINO"
        }
        binding.btncancelar.isEnabled = true
        binding.btnGoBackOrden.isEnabled = true
    }

    private fun cargarDatos() {
        val fc = Calendar.getInstance()
        val df1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        df1.timeZone = TimeZone.getTimeZone("America/Lima")
        fc.time = df1.parse(ordenResponse.fecha) as Date
        val df2 = SimpleDateFormat("yyyy-MM-dd")
        df2.timeZone = TimeZone.getTimeZone("America/Lima")
        binding.tvFechaCompra.text = df2.format(fc.time)

        when (ordenResponse.status) {
            "ENCAMINO" -> {
                binding.tvStatus.text = "En Camino"
                binding.tvStatus.setTextColor(ContextCompat.getColor(applicationContext,R.color.blue_700))
                fc.add(Calendar.DATE, 10)
                binding.tvEntregaEstimada.text = df2.format(fc.time)
            }
            "ENTREGADO" -> {
                binding.tvStatus.text = "Entregado"
                binding.tvStatus.setTextColor(ContextCompat.getColor(applicationContext,R.color.green_accent))
                binding.tvLabelEntregaFecha.text = getString(R.string.entregado_el)
                fc.time = df1.parse(ordenResponse.fechaEntrega) as Date
                binding.tvEntregaEstimada.text = df2.format(fc.time)
                binding.btncancelar.isEnabled = false
            }
            "CANCELADO" -> {
                binding.tvStatus.text = "Cancelado"
                binding.tvStatus.setTextColor(ContextCompat.getColor(applicationContext,R.color.granate_700))
                binding.tvLabelEntregaFecha.text = getString(R.string.cancelado_el)
                fc.time = df1.parse(ordenResponse.fechaEntrega) as Date
                binding.tvEntregaEstimada.text = df2.format(fc.time)
                binding.btncancelar.isEnabled = false
            }
        }

        when (ordenResponse.repartidor) {
            null -> binding.tvRepartidor.text = "Aún sin repartidor"
            else -> binding.tvRepartidor.text = ordenResponse.repartidor?.idRepartidor
        }

        binding.tvDetDireccion.text = ordenResponse.direccion

        binding.rvProductosOrden.adapter = ProductosOrdenAdapter(ordenResponse.ordendetalle)

        binding.tvDetMonto.text = "S/${String.format("%.2f",ordenResponse.monto)}"
        binding.tvDetIGV.text = "S/${String.format("%.2f",ordenResponse.igv)}"
        binding.tvDetTotal.text = "S/${String.format("%.2f",ordenResponse.total)}"
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btncancelar -> getTokenFromDB()
            R.id.btnGoBackOrden -> {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun cancelarOrden() {
        binding.btncancelar.isEnabled = false
        binding.btnGoBackOrden.isEnabled = false

        AlertDialog.Builder(this)
            .setTitle("Cancelar Compra")
            .setMessage("¿Seguro que desea cancelar la compra y el delivery?")
            .setPositiveButton("Sí") { dialogInterface, _ ->
                val fechaActual = Calendar.getInstance()
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                df.timeZone = TimeZone.getTimeZone("America/Lima")

                val ordenPatchRequest = OrdenPatchRequest(
                    df.format(fechaActual.time),
                    "CANCELADO"
                )

                ordenResponse.status = "CANCELADO"
                ordenResponse.fechaEntrega = df.format(fechaActual.time)

                ordenProvider.actualizarOrden(ordenResponse.idOrden, ordenPatchRequest,
                    "Bearer ${token.token}")
                dialogInterface.cancel()
            }
            .setNegativeButton("No"){ dialogInterface, _ ->
                binding.btncancelar.isEnabled = true
                binding.btnGoBackOrden.isEnabled = true
                dialogInterface.cancel()
            }
            .show()
    }

    private fun getTokenFromDB(){
        tokenRoomProvider.obtener().observe(this){
            token = it
            cancelarOrden()
        }
    }
}