package edu.pe.idat.pva.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import edu.pe.idat.pva.R
import edu.pe.idat.pva.apirep.UsuarioApiRepository
import edu.pe.idat.pva.routes.UsuarioRoutes
import edu.pe.idat.pva.databinding.ActivityRegisterBinding
import edu.pe.idat.pva.models.Cliente
import edu.pe.idat.pva.models.UsuarioRequest
import edu.pe.idat.pva.models.UsuarioResponse
import edu.pe.idat.pva.providers.UsuarioProvider
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RegisterActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var usuarioProvider: UsuarioProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usuarioProvider = ViewModelProvider(this)
            .get(UsuarioProvider::class.java)

        val c = Calendar.getInstance()
        c.timeZone = TimeZone.getTimeZone("America/Lima")

        val dpd = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, day)

            updateLabel(c)
        }

        binding.btnGoToMain.setOnClickListener (this)
        binding.btnRegistrar.setOnClickListener(this)
        binding.edtFechaNacimiento.setOnClickListener {
            val d = DatePickerDialog(this,dpd,c.get(Calendar.YEAR),c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH))
            d.datePicker.maxDate = c.timeInMillis
            d.show()
        }

        usuarioProvider.usuarioResponse.observe(this) {
            try {
                obtenerDatosRegistro(it!!)
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext,
                    "ERROR! El correo ya est?? registrado o no es v??lido.",
                    Toast.LENGTH_LONG
                ).show()
                binding.btnRegistrar.isEnabled = true
                binding.btnGoToMain.isEnabled = true
            }
        }
    }

    private fun obtenerDatosRegistro(usuarioResponse: UsuarioResponse) {
        try {
            AlertDialog.Builder(this)
                .setTitle("Registro exitoso")
                .setMessage("El usuario se registr?? correctamente." +
                        " Se le envi?? un enlace de activaci??n al correo ${usuarioResponse.email}.")
                .setPositiveButton("OK") { dialogInterface, i ->
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    dialogInterface.cancel()
                }.show()
            binding.btnRegistrar.isEnabled = true
            binding.btnGoToMain.isEnabled = true
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                "ERROR! El correo ya est?? registrado o no es v??lido.",
                Toast.LENGTH_LONG
            ).show()
            binding.btnRegistrar.isEnabled = true
            binding.btnGoToMain.isEnabled = true
        }
    }

    private fun goToLogin(){
        this.finish()
    }

    override fun onClick(p0: View) {
        when (p0.id){
            R.id.btnGoToMain -> goToLogin()
            R.id.btn_registrar -> registrarUsuario()
        }
    }

    private fun registrarUsuario(){
        binding.btnRegistrar.isEnabled = false
        binding.btnGoToMain.isEnabled = false
        if (validarForm()) {
            val cliente = Cliente(
                binding.edtApellidos.text.toString(),
                binding.edtDNI.text.toString().trim().toInt(),
                binding.edtFechaNacimiento.text.toString().trim(),
                binding.edtNombre.text.toString(),
                binding.edtPhone.text.toString().trim()
            )

            val lstRol = ArrayList<String>()
            lstRol.add("cliente")

            val usuarioRequest = UsuarioRequest(
                cliente,
                binding.edtEmail.text.toString().trim(),
                binding.edtPassword.text.toString().trim(),
                lstRol
            )

            usuarioProvider.registrar(usuarioRequest)
        } else {
            binding.btnRegistrar.isEnabled = true
            binding.btnGoToMain.isEnabled = true
        }
    }

    private fun updateLabel(c: Calendar) {
        val formatoFecha = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(formatoFecha)
        sdf.timeZone = TimeZone.getTimeZone("America/Lima")
        binding.edtFechaNacimiento.setText(sdf.format(c.time))
    }

    private fun validarCorreo(correope: String) : Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(correope).matches()
    }

    private fun validarCampos() : Boolean {
        if(binding.edtNombre.text.toString().trim().isEmpty() ||
            binding.edtApellidos.text.toString().trim().isEmpty() ||
            binding.edtDNI.text.toString().trim().isEmpty() ||
            binding.edtPhone.text.toString().trim().isEmpty() ||
            binding.edtFechaNacimiento.text.toString().trim().isEmpty() ||
            binding.edtEmail.text.toString().trim().isEmpty() ||
            binding.edtPassword.text.toString().trim().isEmpty() ||
            binding.edtPasswordConf.text.toString().trim().isEmpty()){
            return false
        }
        return true
    }

    private fun validarForm() : Boolean {
        if (!validarCampos()){
            Toast.makeText(
                applicationContext,
                "ERROR! Complete todos los campos.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }else if (!validarCorreo(binding.edtEmail.text.toString())) {
            Toast.makeText(
                applicationContext,
                "ERROR! Correo no v??lido.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }else if(binding.edtPassword.text.toString() != binding.edtPasswordConf.text.toString()) {
            Toast.makeText(
                applicationContext,
                "ERROR! Las contrase??as no coinciden.",
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (binding.edtDNI.text.toString().trim().length < 8) {
            Toast.makeText(
                applicationContext,
                "ERROR! Ingrese un DNI v??lido (8 d??gitos).",
                Toast.LENGTH_LONG
            ).show()
            return false
        } else if (binding.edtPhone.text.toString().trim().length < 9) {
            Toast.makeText(
                applicationContext,
                "ERROR! Ingrese un n??mero de celular v??lido (9 d??gitos).",
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return true
    }
}