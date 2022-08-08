package edu.pe.idat.pva.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityDetalleTarjetaBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.models.ResponseHttp
import edu.pe.idat.pva.models.TarjetaPatchRequest
import edu.pe.idat.pva.models.TarjetaResponse
import edu.pe.idat.pva.providers.TarjetaProvider
import edu.pe.idat.pva.providers.TokenRoomProvider
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DetalleTarjetaActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetalleTarjetaBinding
    private lateinit var tarjetaProvider: TarjetaProvider
    private lateinit var tokenRoomProvider: TokenRoomProvider

    private lateinit var tarjetaResponse: TarjetaResponse
    private lateinit var token: TokenEntity

    private var tipo= 0
    private var accion: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleTarjetaBinding.inflate(layoutInflater)
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
        binding.edtAnioCaducidad.setAdapter((adapterAnios))

        tarjetaResponse = intent.getSerializableExtra("tarjeta") as TarjetaResponse

        tarjetaProvider = ViewModelProvider(this)[TarjetaProvider::class.java]
        tokenRoomProvider = ViewModelProvider(this)[TokenRoomProvider::class.java]

        cargarDatos()

        binding.btneditartarjeta.setOnClickListener(this)
        binding.btnborrartarjeta.setOnClickListener(this)
        binding.btnGoBackLstTarjeta.setOnClickListener(this)

        tarjetaProvider.responseHttp.observe(this){
            obtenerRespuesta(it)
        }
    }

    private fun obtenerRespuesta(responseHttp: ResponseHttp) {
        if (responseHttp.isSuccess){
            Toast.makeText(
                applicationContext,
                "Tarjeta $accion",
                Toast.LENGTH_LONG
            ).show()
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            Toast.makeText(
                applicationContext,
                "Hubo un problema con el servicio",
                Toast.LENGTH_LONG
            ).show()
        }
        binding.btneditartarjeta.isEnabled = true
        binding.btnborrartarjeta.isEnabled = true
        binding.btnGoBackLstTarjeta.isEnabled = true
    }

    private fun cargarDatos() {
        when (tarjetaResponse.tipo) {
            1 -> {
                binding.edtTipoTarjeta.setText("Débito",false)
                tipo = 1
            }
            2 -> {
                binding.edtTipoTarjeta.setText("Crédito",false)
                tipo = 2
            }
        }

        binding.edtNumTarjeta.setText(tarjetaResponse.numeroTarjeta)
        binding.edtMesCaducidad.setText(tarjetaResponse.fechaCaducidad.substring(5,7),false)
        binding.edtAnioCaducidad.setText(tarjetaResponse.fechaCaducidad.substring(2,4),false)
        binding.edtNombrePropietario.setText(tarjetaResponse.nombrePropietario)
    }

    private fun seleccionarTipo() {
        when (binding.edtTipoTarjeta.text.toString()) {
            "Débito" -> tipo = 1
            "Crédito" -> tipo = 2
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

    private fun getTokenFromDB(origen: String){
        tokenRoomProvider.obtener().observe(this){
            token = it
            when (origen) {
                "e" -> editarTarjeta()
                "b" -> borrarTarjeta()
            }
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btneditartarjeta -> getTokenFromDB("e")
            R.id.btnborrartarjeta -> getTokenFromDB("b")
            R.id.btnGoBackLstTarjeta -> finish()
        }
    }

    private fun borrarTarjeta() {
        binding.btnborrartarjeta.isEnabled = false
        binding.btneditartarjeta.isEnabled = false
        binding.btnGoBackLstTarjeta.isEnabled = false

        confirmarBorrar()
    }

    private fun confirmarBorrar() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Borrar")
            .setMessage("¿Seguro que desea borrar esta tarjeta?")
            .setPositiveButton("Sí") { dialogInterface, _ ->
                accion = "borrada"

                tarjetaProvider.borrarTarjeta(tarjetaResponse.idTarjeta,
                    "Bearer " + token.token)

                dialogInterface.cancel()
            }
            .setNegativeButton("No"){ dialogInterface, _ ->
                binding.btnborrartarjeta.isEnabled = true
                binding.btneditartarjeta.isEnabled = true
                binding.btnGoBackLstTarjeta.isEnabled = true
                dialogInterface.cancel()
            }
            .show()
    }

    private fun editarTarjeta() {
        binding.btnborrartarjeta.isEnabled = false
        binding.btneditartarjeta.isEnabled = false
        binding.btnGoBackLstTarjeta.isEnabled = false

        if (validarCampos()) {
            confirmarEditar()
        } else {
            binding.btnborrartarjeta.isEnabled = true
            binding.btneditartarjeta.isEnabled = true
            binding.btnGoBackLstTarjeta.isEnabled = true
        }
    }

    private fun confirmarEditar() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Edición")
            .setMessage("¿Seguro que desea editar esta tarjeta?")
            .setPositiveButton("Sí") { dialogInterface, _ ->
                accion = "editada"

                val tarjetaPatchRequest = TarjetaPatchRequest(
                    binding.edtCvv.text.toString(),
                    "20${binding.edtAnioCaducidad.text}-" +
                            "${binding.edtMesCaducidad.text}-28",
                    binding.edtNombrePropietario.text.toString(),
                    binding.edtNumTarjeta.text.toString(),
                    tipo
                )

                tarjetaProvider.editarTarjeta(tarjetaResponse.idTarjeta, tarjetaPatchRequest,
                                            "Bearer " + token.token)

                dialogInterface.cancel()
            }
            .setNegativeButton("No"){ dialogInterface, _ ->
                binding.btnborrartarjeta.isEnabled = true
                binding.btneditartarjeta.isEnabled = true
                binding.btnGoBackLstTarjeta.isEnabled = true
                dialogInterface.cancel()
            }
            .show()
    }
}