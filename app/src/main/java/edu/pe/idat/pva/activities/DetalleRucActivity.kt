package edu.pe.idat.pva.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityDetalleRucBinding
import edu.pe.idat.pva.models.LoginResponse
import edu.pe.idat.pva.models.ResponseHttp
import edu.pe.idat.pva.models.RucPatchRequest
import edu.pe.idat.pva.models.RucResponse
import edu.pe.idat.pva.providers.RucProvider
import edu.pe.idat.pva.utils.SharedPref

class DetalleRucActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetalleRucBinding

    private lateinit var sharedPref: SharedPref
    private lateinit var rucProvider: RucProvider

    private lateinit var ruc: RucResponse

    var accion: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleRucBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)

        rucProvider = ViewModelProvider(this)[RucProvider::class.java]

        ruc = intent.getSerializableExtra("ruc") as RucResponse

        binding.edtRUC.setText(ruc.numeroRuc)

        binding.btnGoListRuc.setOnClickListener(this)
        binding.btneditarruc.setOnClickListener(this)
        binding.btnborrarruc.setOnClickListener(this)

        rucProvider.responseHttp.observe(this) {
            obtenerRespuesta(it)
        }
    }

    private fun obtenerRespuesta(responseHttp: ResponseHttp) {
        if (responseHttp.isSuccess){
            Toast.makeText(
                applicationContext,
                "Número de RUC $accion",
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
        binding.btnGoListRuc.isEnabled = true
        binding.btneditarruc.isEnabled = true
        binding.btnborrarruc.isEnabled = true
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

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnGoListRuc -> finish()
            R.id.btneditarruc -> editarRuc()
            R.id.btnborrarruc -> borrarRuc()
        }
    }

    private fun borrarRuc() {
        binding.btnGoListRuc.isEnabled = false
        binding.btneditarruc.isEnabled = false
        binding.btnborrarruc.isEnabled = false

        confirmarBorrar()
    }

    private fun confirmarBorrar() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Borrar")
            .setMessage("¿Seguro que desea borrar este número de RUC?")
            .setPositiveButton("Sí") { dialogInterface, i ->
                accion = "borrado"

                rucProvider.borrarRuc(ruc.idRuc,
                    "Bearer " + getTokenFromSession()!!.token)

                dialogInterface.cancel()
            }
            .setNegativeButton("No"){ dialogInterface, i ->
                binding.btnGoListRuc.isEnabled = true
                binding.btneditarruc.isEnabled = true
                binding.btnborrarruc.isEnabled = true
                dialogInterface.cancel()
            }
            .show()
    }

    private fun editarRuc() {
        binding.btnGoListRuc.isEnabled = false
        binding.btneditarruc.isEnabled = false
        binding.btnborrarruc.isEnabled = false

        if (validarRuc()) {
            confirmarEditar()
        } else {
            binding.btnGoListRuc.isEnabled = true
            binding.btneditarruc.isEnabled = true
            binding.btnborrarruc.isEnabled = true
        }
    }

    private fun confirmarEditar() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Edición")
            .setMessage("¿Seguro que desea editar este número de RUC?")
            .setPositiveButton("Sí") { dialogInterface, i ->
                accion = "editado"

                val rucPatchRequest = RucPatchRequest(
                    binding.edtRUC.text.toString()
                )

                rucProvider.editarRuc(ruc.idRuc,rucPatchRequest,
                                      "Bearer " + getTokenFromSession()!!.token)

                dialogInterface.cancel()
            }
            .setNegativeButton("No"){ dialogInterface, i ->
                binding.btnGoListRuc.isEnabled = true
                binding.btneditarruc.isEnabled = true
                binding.btnborrarruc.isEnabled = true
                dialogInterface.cancel()
            }
            .show()
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
}