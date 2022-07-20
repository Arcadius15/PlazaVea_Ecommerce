package edu.pe.idat.pva.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityCambiarContraBinding

class CambiarContraActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityCambiarContraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCambiarContraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoBackMain.setOnClickListener(this)
        binding.btnCambiar.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.btnGoBackMain -> startActivity(Intent(this,MainActivity::class.java))
            R.id.btn_cambiar -> cambiarContra()
        }
    }

    private fun cambiarContra() {
        binding.btnGoBackMain.isEnabled = false
        binding.btnCambiar.isEnabled = false
        if (validarCampos()){

        } else {
            binding.btnGoBackMain.isEnabled = true
            binding.btnCambiar.isEnabled = true
        }
    }

    private fun validarCampos(): Boolean {
        if (binding.edtEmail.text.toString().isNullOrBlank()) {
            Toast.makeText(
                applicationContext,
                "Debe ingresar su correo.",
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

        if (!binding.edtEmail.text.toString().trim().isEmailValid()) {
            Toast.makeText(
                applicationContext,
                "Debe ingresar un correo válido.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}