package edu.pe.idat.pva.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityCambiarContraBinding
import edu.pe.idat.pva.db.entity.UsuarioEntity
import edu.pe.idat.pva.models.response.Mensaje
import edu.pe.idat.pva.models.request.UsuarioPswRequest
import edu.pe.idat.pva.providers.UsuarioProvider
import edu.pe.idat.pva.providers.UsuarioRoomProvider

class CambiarContraActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCambiarContraBinding
    private lateinit var usuarioProvider: UsuarioProvider
    private lateinit var usuarioRoomProvider: UsuarioRoomProvider

    private lateinit var usuario: UsuarioEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCambiarContraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioProvider = ViewModelProvider(this)[UsuarioProvider::class.java]
        usuarioRoomProvider = ViewModelProvider(this)[UsuarioRoomProvider::class.java]

        binding.btnGoBackHome.setOnClickListener(this)
        binding.btnCambiar.setOnClickListener(this)

        usuarioProvider.mensaje.observe(this){
            try {
                obtenerMensaje(it!!)
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext,
                    "ERROR! Ocurrió un problema con el servicio, intente de nuevo más tarde.",
                    Toast.LENGTH_LONG
                ).show()
                binding.btnGoBackHome.isEnabled = true
                binding.btnCambiar.isEnabled = true
            }
        }
    }

    private fun obtenerMensaje(mensaje: Mensaje) {
        when (mensaje.mensaje) {
            "Contraseña Actualizada exitosamente" -> {
                Toast.makeText(
                    applicationContext,
                    mensaje.mensaje,
                    Toast.LENGTH_LONG
                ).show()
                gotoHome()
            }
            "Contraseña Invalida" -> {
                Toast.makeText(
                    applicationContext,
                    "La contraseña actual ingresada es incorrecta",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                Toast.makeText(
                    applicationContext,
                    "Error: ${mensaje.mensaje}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.btnGoBackHome.isEnabled = true
        binding.btnCambiar.isEnabled = true
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.btnGoBackHome -> gotoHome()
            R.id.btn_cambiar -> getUserFromDB()
        }
    }

    private fun cambiarContra() {
        binding.btnGoBackHome.isEnabled = false
        binding.btnCambiar.isEnabled = false
        if (validarCampos()){
            val usuarioPswRequest = UsuarioPswRequest(
                usuario.email,
                binding.edtNewPassword.text.toString().trim(),
                binding.edtOldPassword.text.toString().trim()
            )

            usuarioProvider.editPassword(usuarioPswRequest)
        } else {
            binding.btnGoBackHome.isEnabled = true
            binding.btnCambiar.isEnabled = true
        }
    }

    private fun validarCampos(): Boolean {
        if (binding.edtOldPassword.text.toString().isNullOrBlank()) {
            Toast.makeText(
                applicationContext,
                "Debe ingresar su contraseña actual.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (binding.edtNewPassword.text.toString().isNullOrBlank()) {
            Toast.makeText(
                applicationContext,
                "Debe ingresar una nueva contraseña.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (binding.edtPasswordConf.text.toString().isNullOrBlank() ||
            binding.edtPasswordConf.text.toString() != binding.edtNewPassword.text.toString()) {
            Toast.makeText(
                applicationContext,
                "Las contraseñas no coinciden.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    private fun gotoHome(){
        val i = Intent(this, HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getUserFromDB(){
        usuarioRoomProvider.obtener().observe(this){
            usuario = it
            cambiarContra()
        }
    }
}