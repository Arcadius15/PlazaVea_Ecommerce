package edu.pe.idat.pva.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityRegTarjetaBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.db.entity.UsuarioEntity
import edu.pe.idat.pva.models.request.ClienteIDRequest
import edu.pe.idat.pva.models.response.ResponseHttp
import edu.pe.idat.pva.models.request.TarjetaRequest
import edu.pe.idat.pva.providers.ClienteProvider
import edu.pe.idat.pva.providers.TokenRoomProvider
import edu.pe.idat.pva.providers.UsuarioRoomProvider
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RegTarjetaActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegTarjetaBinding
    private lateinit var clienteProvider: ClienteProvider
    private lateinit var usuarioRoomProvider: UsuarioRoomProvider
    private lateinit var tokenRoomProvider: TokenRoomProvider

    private lateinit var usuario: UsuarioEntity
    private lateinit var token: TokenEntity

    private var tipo= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegTarjetaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tipos = arrayOf("Crédito","Débito")

        val meses = arrayOf("01","02","03","04","05","06","07","08","09","10","11","12")

        val df: DateFormat = SimpleDateFormat("yy")
        val twodigistanio: Int = df.format(Calendar.getInstance().time).toInt()
        val anios = (twodigistanio..twodigistanio+10).toList()

        val adapterTipos = ArrayAdapter(this,R.layout.drop_down_item, tipos)
        val adapterMeses = ArrayAdapter(this,R.layout.drop_down_item, meses)
        val adapterAnios = ArrayAdapter(this,R.layout.drop_down_item, anios)

        binding.edtTipoTarjeta.setAdapter(adapterTipos)
        binding.edtTipoTarjeta.setOnItemClickListener { _, _, _, _ -> seleccionarTipo() }

        binding.edtMesCaducidad.setAdapter(adapterMeses)
        binding.edtAnioCaducidad.setAdapter(adapterAnios)

        clienteProvider = ViewModelProvider(this)[ClienteProvider::class.java]
        usuarioRoomProvider = ViewModelProvider(this)[UsuarioRoomProvider::class.java]
        tokenRoomProvider = ViewModelProvider(this)[TokenRoomProvider::class.java]

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

            setResult(Activity.RESULT_OK)
            finish()
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
            R.id.btnregistrartarjeta -> getUserFromDB()
            R.id.btnGoBackTarjeta -> finish()
        }
    }

    private fun registrarTarjeta() {
        binding.btnregistrartarjeta.isEnabled = false

        if (validarCampos()) {
            val clienteIDRequest = ClienteIDRequest(
                usuario.idCliente
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
                                            "Bearer ${token.token}")
        } else {
            binding.btnregistrartarjeta.isEnabled = true
        }
    }

    private fun validarCampos(): Boolean {
        if(binding.edtTipoTarjeta.text.toString().isBlank() ||
            binding.edtNumTarjeta.text.toString().isBlank() ||
            binding.edtMesCaducidad.text.toString().isBlank() ||
            binding.edtAnioCaducidad.text.toString().isBlank() ||
            binding.edtCvv.text.toString().isBlank() ||
            binding.edtNombrePropietario.text.toString().isBlank()){
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

    private fun getUserFromDB(){
        usuarioRoomProvider.obtener().observe(this){
            usuario = it
            getTokenFromDB()
        }
    }

    private fun getTokenFromDB(){
        tokenRoomProvider.obtener().observe(this){
            token = it
            registrarTarjeta()
        }
    }
}