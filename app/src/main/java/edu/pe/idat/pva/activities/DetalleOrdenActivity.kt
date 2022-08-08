package edu.pe.idat.pva.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.adapter.ProductosOrdenAdapter
import edu.pe.idat.pva.databinding.ActivityDetalleOrdenBinding
import edu.pe.idat.pva.models.LoginResponse
import edu.pe.idat.pva.models.OrdenResponse
import edu.pe.idat.pva.models.ResponseHttp
import edu.pe.idat.pva.providers.OrdenProvider
import edu.pe.idat.pva.utils.SharedPref
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class DetalleOrdenActivity : AppCompatActivity() {

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
        binding.tvStatus.text = ordenResponse.status
        binding.tvDetDireccion.text = ordenResponse.direccion

      /* val fc = Calendar.getInstance()
        val df1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        df1.timeZone = TimeZone.getTimeZone("America/Lima")
        fc.time = df1.parse(ordenResponse.fechaEntrega) as Date
        val df2 = SimpleDateFormat("yyyy-MM-dd")
        df2.timeZone = TimeZone.getTimeZone("America/Lima")
        fc.add(Calendar.DATE, 10)
        binding.tvEntregaEstimada.setText(df2.format(fc.time))*/

        binding.rvProductosOrden.adapter = ProductosOrdenAdapter(ordenResponse.ordendetalle)

        binding.tvDetMonto.text = "S/${ordenResponse.monto}"
        binding.tvDetIGV.text = "S/${ordenResponse.igv}"
        binding.tvDetTotal.text = "S/${ordenResponse.total}"
    }

}