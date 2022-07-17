package edu.pe.idat.pva.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityMainBinding
import edu.pe.idat.pva.models.*
import edu.pe.idat.pva.providers.UsuarioProvider
import edu.pe.idat.pva.utils.SharedPref

class MainActivity : AppCompatActivity() , View.OnClickListener {

    var imageViewGoToRegister: ImageView? = null
    var editTextEmail: EditText? = null
    var editTextPassword: EditText? = null
    var buttonLogin: Button? = null
    private lateinit var binding: ActivityMainBinding

    private lateinit var usuarioProvider: UsuarioProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usuarioProvider = ViewModelProvider(this)
            .get(UsuarioProvider::class.java)

        imageViewGoToRegister = findViewById(R.id.btn_go_register)
        editTextEmail = findViewById(R.id.edTextEmail)
        editTextPassword = findViewById(R.id.edTextPassword)
        buttonLogin = findViewById(R.id.btn_login)

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

        getUserFromSession()
    }

    private fun obtenerDatosUsuario(usuarioResponse: UsuarioResponse) {
        if(usuarioResponse != null){
            Toast.makeText(
                applicationContext,
                "Sesión Iniciada",
                Toast.LENGTH_LONG
            ).show()
            binding.btnLogin.isEnabled = true
            binding.btnGoRegister.isEnabled = true
            saveUserInSession(usuarioResponse)
        } else {
            Toast.makeText(
                applicationContext,
                "ERROR! Ocurrió un error con el servicio web.",
                Toast.LENGTH_LONG
            ).show()
            binding.btnLogin.isEnabled = true
            binding.btnGoRegister.isEnabled = true
        }
    }

    private fun obtenerDatosLogin(loginResponse: LoginResponse) {
        try{
            saveTokenInSession(loginResponse)

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
        if (isValidForm(binding.edTextEmail.text.toString(),
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
    }

    private fun saveUserInSession(usuarioResponse: UsuarioResponse){
        val sharedPref = SharedPref(this)
        sharedPref.save("user", usuarioResponse)

        gotoHome()
    }

    private fun saveTokenInSession(loginResponse: LoginResponse){
        val sharedPref = SharedPref(this)
        sharedPref.save("token", loginResponse)
    }

    private fun getUserFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()

        if(!sharedPref.getData("user").isNullOrBlank()){
            val user = gson.fromJson(sharedPref.getData("user"), UsuarioResponse::class.java)
            val token = gson.fromJson(sharedPref.getData("token"), LoginResponse::class.java)

            Toast.makeText(
                applicationContext,
                "Bienvenido, ${user.cliente.nombre}.",
                Toast.LENGTH_LONG
            ).show()

            gotoHome()
        }
    }

    fun String.isEmailValid(): Boolean {
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