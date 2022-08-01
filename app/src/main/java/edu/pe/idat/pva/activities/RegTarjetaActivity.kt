package edu.pe.idat.pva.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.pasarela.ResumenActivity
import edu.pe.idat.pva.databinding.ActivityRegTarjetaBinding
import edu.pe.idat.pva.models.*
import edu.pe.idat.pva.providers.ClienteProvider
import edu.pe.idat.pva.utils.SharedPref
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RegTarjetaActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var sharedPref: SharedPref

    private lateinit var binding: ActivityRegTarjetaBinding
    private lateinit var clienteProvider: ClienteProvider

    var tipo= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegTarjetaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var tipos = arrayOf("Crédito","Débito")

        var meses = arrayOf("01","02","03","04","05","06","07","08","09","10","11","12")

        val df: DateFormat = SimpleDateFormat("yy")
        val twodigistanio: Int = df.format(Calendar.getInstance().time).toInt()
        var anios = (twodigistanio..twodigistanio+10).toList()

        var adapterTipos = ArrayAdapter<String>(this,R.layout.drop_down_item, tipos)
        var adapterMeses = ArrayAdapter<String>(this,R.layout.drop_down_item, meses)
        var adapterAnios = ArrayAdapter<Int>(this,R.layout.drop_down_item, anios)

        binding.edtTipoTarjeta.setAdapter(adapterTipos)
        binding.edtTipoTarjeta.setOnItemClickListener { adapterView, view, i, l -> seleccionarTipo() }

        binding.edtMesCaducidad.setAdapter(adapterMeses)
        binding.edtAnioCaducidad.setAdapter((adapterAnios))

        sharedPref = SharedPref(this)
        clienteProvider = ViewModelProvider(this)[ClienteProvider::class.java]

        binding.btnregistrartarjeta.setOnClickListener(this)
        binding.btnGoBackTarjeta.setOnClickListener(this)
        
        clienteProvider.responseHttp.observe(this){
            obtenerRespuesta(it!!)
        }
    }

    private fun obtenerRespuesta(responseHttp: ResponseHttp) {
        if(responseHttp.isSuccess){
            Toast.makeText(
                applicationContext,
                "Tarjeta Registrada.",
                Toast.LENGTH_LONG
            ).show()
            if (intent.getStringExtra("origen").toString() == "lista") {
                setResult(Activity.RESULT_OK)
                finish()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "ERROR! Hubo un problema, verifique los datos.",
                Toast.LENGTH_LONG
            ).show()
        }
        binding.btnregistrartarjeta.isEnabled = true
    }

    private fun seleccionarTipo() {
        when (binding.edtTipoTarjeta.text.toString()) {
            "Débito" -> tipo = 1
            "Crédito" -> tipo = 2
        }
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.btnregistrartarjeta -> registrarTarjeta()
            R.id.btnGoBackTarjeta -> finish()
        }
    }

    private fun registrarTarjeta() {
        binding.btnregistrartarjeta.isEnabled = false

        if (validarCampos()) {
            val clienteIDRequest = ClienteIDRequest(
                getUserFromSession()!!.cliente.idCliente
            )

            val tarjetaRequest = TarjetaRequest(
                binding.edtCvv.text.toString(),
                "20${binding.edtAnioCaducidad.text}-" +
                        "${binding.edtMesCaducidad.text}-28",
                binding.edtNombrePropietario.text.toString(),
                binding.edtNumTarjeta.text.toString(),
                tipo,
                clienteIDRequest
            )
            
            clienteProvider.registrarTarjeta(tarjetaRequest,
                                            "Bearer ${getTokenFromSession()!!.token}")
        } else {
            binding.btnregistrartarjeta.isEnabled = true
        }
    }

    private fun validarCampos(): Boolean {
        if(binding.edtTipoTarjeta.text.toString().isNullOrBlank() ||
            binding.edtNumTarjeta.text.toString().isNullOrBlank() ||
            binding.edtMesCaducidad.text.toString().isNullOrBlank() ||
            binding.edtAnioCaducidad.text.toString().isNullOrBlank() ||
            binding.edtCvv.text.toString().isNullOrBlank() ||
            binding.edtNombrePropietario.text.toString().isNullOrBlank()){
            Toast.makeText(
                applicationContext,
                "Complete todos los campos.",
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if(binding.edtNumTarjeta.text.toString().trim().length < 16){
            Toast.makeText(
                applicationContext,
                "Ingrese una tarjeta válida.",
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if(binding.edtCvv.text.toString().trim().length <3){
            Toast.makeText(
                applicationContext,
                "Ingrese un código CVV válido.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
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