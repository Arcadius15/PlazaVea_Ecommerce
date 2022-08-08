package edu.pe.idat.pva.activities

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.pe.idat.pva.databinding.ActivityRegRucBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.db.entity.UsuarioEntity
import edu.pe.idat.pva.models.ClienteIDRequest
import edu.pe.idat.pva.models.ResponseHttp
import edu.pe.idat.pva.models.RucRequest
import edu.pe.idat.pva.providers.ClienteProvider
import edu.pe.idat.pva.providers.TokenRoomProvider
import edu.pe.idat.pva.providers.UsuarioRoomProvider

class RegRucActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegRucBinding
    private lateinit var clienteProvider: ClienteProvider
    private lateinit var usuarioRoomProvider: UsuarioRoomProvider
    private lateinit var tokenRoomProvider: TokenRoomProvider

    private lateinit var usuario: UsuarioEntity
    private lateinit var token: TokenEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegRucBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clienteProvider = ViewModelProvider(this)[ClienteProvider::class.java]
        usuarioRoomProvider = ViewModelProvider(this)[UsuarioRoomProvider::class.java]
        tokenRoomProvider = ViewModelProvider(this)[TokenRoomProvider::class.java]

        binding.btnregistrarruc.setOnClickListener{ getUserFromDB() }
        binding.btnGoBackRuc.setOnClickListener{ finish() }

        clienteProvider.responseHttp.observe(this){
            obtenerRespuesta(it!!)
        }
    }

    private fun registrarRuc() {
        binding.btnregistrarruc.isEnabled = false
        if (validarRuc()){
            val clienteIDRequest = ClienteIDRequest(
                usuario.idCliente
            )

            val rucRequest = RucRequest(
                binding.edtRUC.text.toString().trim(),
                clienteIDRequest
            )

            clienteProvider.registrarRuc(rucRequest,
                "Bearer ${token.token}")
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

            setResult(Activity.RESULT_OK)
            finish()
        } else {
            Toast.makeText(
                applicationContext,
                "Ingrese un número de RUC válido.",
                Toast.LENGTH_LONG
            ).show()
        }
        binding.btnregistrarruc.isEnabled = true
    }

    private fun validarRuc() : Boolean {
        if (binding.edtRUC.text.toString().isNullOrBlank()){
            Toast.makeText(
                applicationContext,
                "Ingrese un número de RUC.",
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (binding.edtRUC.text.toString().trim().length < 11) {
            Toast.makeText(
                applicationContext,
                "Ingrese un número de RUC válido (11 dígitos).",
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (binding.edtRUC.text.toString().trim().take(2) != "10"){
            Toast.makeText(
                applicationContext,
                "Ingrese un número de RUC válido (empieza con 10).",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }

    private fun getUserFromDB(){
        usuarioRoomProvider.obtener().observe(this){
            usuario = it
            getTokenFromDB()
        }
    }

    private fun getTokenFromDB(){
        tokenRoomProvider.obtener().observe(this){
            token = it
            registrarRuc()
        }
    }
}