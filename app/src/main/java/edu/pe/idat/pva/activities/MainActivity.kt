package edu.pe.idat.pva.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityMainBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.db.entity.UsuarioEntity
import edu.pe.idat.pva.models.LoginRequest
import edu.pe.idat.pva.models.LoginResponse
import edu.pe.idat.pva.models.UsuarioResponse
import edu.pe.idat.pva.providers.TokenRoomProvider
import edu.pe.idat.pva.providers.UsuarioProvider
import edu.pe.idat.pva.providers.UsuarioRoomProvider
import edu.pe.idat.pva.utils.SharedPref

class MainActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var usuarioProvider: UsuarioProvider
    private lateinit var usuarioRoomProvider: UsuarioRoomProvider
    private lateinit var tokenRoomProvider: TokenRoomProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usuarioProvider = ViewModelProvider(this)[UsuarioProvider::class.java]
        usuarioRoomProvider = ViewModelProvider(this)[UsuarioRoomProvider::class.java]
        tokenRoomProvider = ViewModelProvider(this)[TokenRoomProvider::class.java]

        binding.btnGoRegister.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)

        usuarioProvider.loginResponse.observe(this){
            try {
                obtenerDatosLogin(it!!)
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext,
                    "ERROR! Las credenciales son incorrectas o falta confirmar correo.",
                    Toast.LENGTH_LONG
                ).show()
                binding.btnLogin.isEnabled = true
                binding.btnGoRegister.isEnabled = true
            }
        }

        usuarioProvider.usuarioResponse.observe(this){
            obtenerDatosUsuario(it!!)
        }

        checkUserSession()
    }

    private fun obtenerDatosUsuario(usuarioResponse: UsuarioResponse) {
        val usuarioEntity = UsuarioEntity(
            usuarioResponse.idUsuario,
            usuarioResponse.email,
            usuarioResponse.activo,
            usuarioResponse.blocked,
            usuarioResponse.cliente.idCliente,
            usuarioResponse.cliente.nombre,
            usuarioResponse.cliente.apellidos,
            usuarioResponse.cliente.dni,
            usuarioResponse.cliente.numTelefonico
        )

        if (SharedPref(this).getSomeBooleanValue("mantener")) {
            usuarioRoomProvider.actualizar(usuarioEntity)
        } else {
            usuarioRoomProvider.insertar(usuarioEntity)
            if (binding.chkmantener.isChecked) {
                SharedPref(this).setSomeBooleanValue("mantener",true)
            }
        }

        Toast.makeText(
            applicationContext,
            "Sesión Iniciada",
            Toast.LENGTH_LONG
        ).show()

        gotoHome()
        binding.btnLogin.isEnabled = true
        binding.btnGoRegister.isEnabled = true
    }

    private fun obtenerDatosLogin(loginResponse: LoginResponse) {
        try{
            val tokenEntity = TokenEntity(
                loginResponse.token
            )

            if (SharedPref(this).getSomeBooleanValue("mantener")) {
                tokenRoomProvider.actualizar(tokenEntity)
            } else {
                tokenRoomProvider.insertar(tokenEntity)
            }

            val loginRequest = LoginRequest(
                binding.edTextEmail.text.toString().trim(),
                binding.edTextPassword.text.toString().trim()
            )

            usuarioProvider.getUserByEmail(loginRequest,
                "Bearer " + loginResponse.token)
        } catch (e: Exception) {
            Toast.makeText(
                applicationContext,
                "ERROR! Las credenciales son incorrectas o falta confirmar correo.",
                Toast.LENGTH_LONG
            ).show()
            binding.btnLogin.isEnabled = true
            binding.btnGoRegister.isEnabled = true
        }
    }

    private fun login() {
        binding.btnLogin.isEnabled = false
        binding.btnGoRegister.isEnabled = false
        if (isValidForm(binding.edTextEmail.text.toString().trim(),
                binding.edTextPassword.text.toString())){
            val loginRequest = LoginRequest(
                binding.edTextEmail.text.toString().trim(),
                binding.edTextPassword.text.toString().trim()
            )
            usuarioProvider.autenticar(loginRequest)
        } else {
            binding.btnLogin.isEnabled = true
            binding.btnGoRegister.isEnabled = true
        }
    }

    private fun gotoHome(){
        val i = Intent(this, HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
        finish()
    }

    private fun checkUserSession(){
        val sharedPref = SharedPref(this)

        if (sharedPref.getSomeBooleanValue("mantener")) {
            usuarioRoomProvider.obtener().observe(this){
                usuario -> usuario?.let {
                    Toast.makeText(
                        applicationContext,
                        "Bienvenido de nuevo, ${usuario.nombre}.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            gotoHome()
        } else {
            usuarioRoomProvider.eliminarTodo()
            tokenRoomProvider.eliminarToken()
        }
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun isValidForm(email: String, password: String): Boolean {

        if (email.isBlank()) {
            Toast.makeText(
                applicationContext,
                "Debe ingresar su correo.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (password.isBlank()) {
            Toast.makeText(
                applicationContext,
                "Debe ingresar su contraseña.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        if (!email.isEmailValid()) {
            Toast.makeText(
                applicationContext,
                "Debe ingresar un correo válido.",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    private fun goToRegister(){
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

    override fun onClick(p0: View) {
        when (p0.id){
            R.id.btn_go_register -> goToRegister()
            R.id.btn_login -> login()
        }
    }
}