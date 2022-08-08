package edu.pe.idat.pva.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import edu.pe.idat.pva.R
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
    private lateinit var sharedPref: SharedPref
    private lateinit var ordenResponse: OrdenResponse
    private lateinit var ordenProvider: OrdenProvider


    var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleOrdenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)

        ordenResponse = intent.getSerializableExtra("orden") as OrdenResponse

        ordenProvider = ViewModelProvider(this)[OrdenProvider::class.java]

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
        binding.tvStatus.setText(ordenResponse.status)
        binding.tvDetDireccion.setText(ordenResponse.direccion)

      /* val fc = Calendar.getInstance()
        val df1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        df1.timeZone = TimeZone.getTimeZone("America/Lima")
        fc.time = df1.parse(ordenResponse.fechaEntrega) as Date
        val df2 = SimpleDateFormat("yyyy-MM-dd")
        df2.timeZone = TimeZone.getTimeZone("America/Lima")
        fc.add(Calendar.DATE, 10)
        binding.tvEntregaEstimada.setText(df2.format(fc.time))*/


        val sb = StringBuilder()



        ordenResponse.ordendetalle.forEach {
            sb.append("${it.producto.nombre} x ${it.cantidad} ..... S/${it.precio * it.cantidad}\n")
        }

        binding.tvdetProductoslista.setText(sb.toString())

        binding.tvDetMonto.setText("S/" + ordenResponse.monto.toString())
        binding.tvDetIGV.setText("S/" + ordenResponse.igv.toString())
        binding.tvDetTotal.setText("S/" + ordenResponse.total.toString())

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