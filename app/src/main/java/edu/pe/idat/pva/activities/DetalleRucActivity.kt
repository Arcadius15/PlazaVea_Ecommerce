package edu.pe.idat.pva.activities

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityDetalleRucBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.models.response.ResponseHttp
import edu.pe.idat.pva.models.request.RucPatchRequest
import edu.pe.idat.pva.models.response.RucResponse
import edu.pe.idat.pva.providers.RucProvider
import edu.pe.idat.pva.providers.TokenRoomProvider

class DetalleRucActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetalleRucBinding

    private lateinit var rucProvider: RucProvider
    private lateinit var tokenRoomProvider: TokenRoomProvider

    private lateinit var ruc: RucResponse
    private lateinit var token: TokenEntity

    private var accion: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleRucBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rucProvider = ViewModelProvider(this)[RucProvider::class.java]
        tokenRoomProvider = ViewModelProvider(this)[TokenRoomProvider::class.java]

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

    private fun getTokenFromDB(origen: String){
        tokenRoomProvider.obtener().observe(this){
            token = it
            when (origen) {
                "e" -> editarRuc()
                "b" -> borrarRuc()
            }
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btnGoListRuc -> finish()
            R.id.btneditarruc -> getTokenFromDB("e")
            R.id.btnborrarruc -> getTokenFromDB("b")
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
            .setPositiveButton("Sí") { dialogInterface, _ ->
                accion = "borrado"

                rucProvider.borrarRuc(ruc.idRuc,
                    "Bearer " + token.token)

                dialogInterface.cancel()
            }
            .setNegativeButton("No"){ dialogInterface, _ ->
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
            .setPositiveButton("Sí") { dialogInterface, _ ->
                accion = "editado"

                val rucPatchRequest = RucPatchRequest(
                    binding.edtRUC.text.toString()
                )

                rucProvider.editarRuc(ruc.idRuc,rucPatchRequest,
                                      "Bearer " + token.token)

                dialogInterface.cancel()
            }
            .setNegativeButton("No"){ dialogInterface, _ ->
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