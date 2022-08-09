package edu.pe.idat.pva.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.pe.idat.pva.R
import edu.pe.idat.pva.adapter.ProductosOrdenAdapter
import edu.pe.idat.pva.databinding.ActivityDetalleOrdenBinding
import edu.pe.idat.pva.models.OrdenResponse
import edu.pe.idat.pva.models.ResponseHttp
import edu.pe.idat.pva.providers.OrdenProvider
import java.text.SimpleDateFormat
import java.util.*

class DetalleOrdenActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetalleOrdenBinding
    private lateinit var ordenResponse: OrdenResponse
    private lateinit var ordenProvider: OrdenProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleOrdenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordenResponse = intent.getSerializableExtra("orden") as OrdenResponse

        ordenProvider = ViewModelProvider(this)[OrdenProvider::class.java]

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

            setResult(Activity.RESULT_OK)
            finish()
        } else {
            Toast.makeText(
                applicationContext,
                "Hubo un problema con el servicio",
                Toast.LENGTH_LONG
            ).show()
        }
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
            }
            "CANCELADO" -> {
                binding.tvStatus.text = "Cancelado"
                binding.tvStatus.setTextColor(ContextCompat.getColor(applicationContext,R.color.granate_700))
                binding.tvLabelEntregaFecha.text = getString(R.string.cancelado_el)
                fc.time = df1.parse(ordenResponse.fechaEntrega) as Date
                binding.tvEntregaEstimada.text = df2.format(fc.time)
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
            R.id.btncancelar -> cancelarOrden()
            R.id.btnGoBackOrden -> finish()
        }
    }

    private fun cancelarOrden() {

    }

}