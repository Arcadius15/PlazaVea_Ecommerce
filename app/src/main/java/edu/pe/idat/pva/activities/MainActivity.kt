package edu.pe.idat.pva.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityMainBinding
import edu.pe.idat.pva.models.LoginRequest
import edu.pe.idat.pva.models.LoginResponse
import edu.pe.idat.pva.models.ResponseHttp
import edu.pe.idat.pva.models.User
import edu.pe.idat.pva.providers.UsersProvider
import edu.pe.idat.pva.providers.UsuarioProvider
import edu.pe.idat.pva.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() , View.OnClickListener {

    var imageViewGoToRegister: ImageView? = null
    var editTextEmail: EditText? = null
    var editTextPassword: EditText? = null
    var buttonLogin: Button? = null
    //var usersProvider = UsersProvider()
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
            obtenerDatosLogin(it!!)
        }

        getUserFromSession()
    }

    private fun obtenerDatosLogin(loginResponse: LoginResponse) {
        if(loginResponse != null){
            Toast.makeText(
                applicationContext,
                "Sesión Iniciada",
                Toast.LENGTH_LONG
            ).show()
            startActivity(Intent(applicationContext, HomeActivity::class.java))
        } else {
            Toast.makeText(
                applicationContext,
                "ERROR! Las credenciales son incorrectas o falta confirmar correo.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun login() {
        /*val email = editTextEmail?.text.toString() // NULL POINTER EXCEPTION
        val password = editTextPassword?.text.toString()

        if (isValidForm(email, password)) {
            usersProvider.login(email, password)?.enqueue(object : Callback<ResponseHttp> {
                override fun onResponse(
                    call: Call<ResponseHttp>,
                    response: Response<ResponseHttp>
                ) {
                    Log.d("MainActivity", "Response: ${response.body()}" )

                    if(response.body()?.isSuccess == true){
                        Toast.makeText(this@MainActivity, response.body()?.message, Toast.LENGTH_LONG).show()

                        saveUserInSession(response.body()?.data.toString())
                    }
                    else{
                        Toast.makeText(this@MainActivity, "Los datos son incorrectos", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseHttp>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Hubo un error ${t.message}", Toast.LENGTH_LONG).show()
                }

            })

        }
        else {
            Toast.makeText(this, "No es valido", Toast.LENGTH_LONG).show()
        }

        Log.d("MainActivity", "El email es: $email")*/
        if (isValidForm(binding.edTextEmail.text.toString(),
                binding.edTextPassword.text.toString())){
            val loginRequest = LoginRequest(
                binding.edTextEmail.text.toString().trim(),
                binding.edTextPassword.text.toString().trim()
            )
            usuarioProvider.autenticar(loginRequest)
        } else {
            Toast.makeText(
                applicationContext,
                "ERROR! Complete los campos e ingrese un correo válido.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun gotoHome(){
        val i = Intent(this, HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun saveUserInSession(data: String){
        val sharedPref = SharedPref(this)
        val gson = Gson()
        val user = gson.fromJson(data, User::class.java)
        sharedPref.save("user", user)

        gotoHome()
    }

    private fun getUserFromSession(){
        val sharedPref = SharedPref(this)
        val gson = Gson()

        if(!sharedPref.getData("user").isNullOrBlank()){
            val user = gson.fromJson(sharedPref.getData("user"), User::class.java)

            gotoHome()
        }
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun isValidForm(email: String, password: String): Boolean {

        if (email.isBlank()) {
            return false
        }

        if (password.isBlank()) {
            return false
        }

        if (!email.isEmailValid()) {
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