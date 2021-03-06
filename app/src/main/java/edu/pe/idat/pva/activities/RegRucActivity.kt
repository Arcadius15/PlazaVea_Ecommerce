package edu.pe.idat.pva.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import edu.pe.idat.pva.databinding.ActivityRegRucBinding
import edu.pe.idat.pva.models.*
import edu.pe.idat.pva.providers.ClienteProvider
import edu.pe.idat.pva.utils.SharedPref

class RegRucActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPref

    private lateinit var binding: ActivityRegRucBinding
    private lateinit var clienteProvider: ClienteProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegRucBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPref(this)
        clienteProvider = ViewModelProvider(this)[ClienteProvider::class.java]

        binding.btnregistrarruc.setOnClickListener{ registrarRuc() }
        binding.btnGoBackRuc.setOnClickListener{ finish() }

        clienteProvider.responseHttp.observe(this){
            obtenerRespuesta(it!!)
        }
    }

    private fun registrarRuc() {
        binding.btnregistrarruc.isEnabled = false
        if (validarRuc()){
            val clienteIDRequest = ClienteIDRequest(
                getUserFromSession()!!.cliente.idCliente
            )

            val rucRequest = RucRequest(
                binding.edtRUC.text.toString().trim(),
                clienteIDRequest
            )

            clienteProvider.registrarRuc(rucRequest,
                "Bearer ${getTokenFromSession()!!.token}")
        } else {
            binding.btnregistrarruc.isEnabled = true
        }
    }

    private fun obtenerRespuesta(responseHttp: ResponseHttp) {
        if (responseHttp.isSuccess){
            Toast.makeText(
                applicationContext,
                "RUC registrado.",
                Toast.LENGTH_LONG
            ).show()
            startActivity(Intent(this,DireccionRegistroActivity::class.java)
                .putExtra("tipo",intent.getStringExtra("tipo").toString())
                .putExtra("ruc",binding.edtRUC.text.toString()))
        } else {
            Toast.makeText(
                applicationContext,
                "Ingrese un n??mero de RUC v??lido.",
                Toast.LENGTH_LONG
            ).show()
        }
        binding.btnregistrarruc.isEnabled = true
    }

    private fun validarRuc() : Boolean {
        if (binding.edtRUC.text.toString().isNullOrBlank()){
            Toast.makeText(
                applicationContext,
                "Ingrese un n??mero de RUC.",
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (binding.edtRUC.text.toString().trim().length < 11) {
            Toast.makeText(
                applicationContext,
                "Ingrese un n??mero de RUC v??lido (11 d??gitos).",
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (binding.edtRUC.text.toString().trim().take(2) != "10"){
            Toast.makeText(
                applicationContext,
                "Ingrese un n??mero de RUC v??lido (empieza con 10).",
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