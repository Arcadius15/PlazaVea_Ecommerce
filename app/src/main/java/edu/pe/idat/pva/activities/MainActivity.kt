package edu.pe.idat.pva.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var imageViewGoToRegister: ImageView? = null
    var editTextEmail: EditText? = null
    var editTextPassword: EditText? = null
    var buttonLogin: Button? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageViewGoToRegister = findViewById(R.id.btn_go_register)
        editTextEmail = findViewById(R.id.edTextEmail)
        editTextPassword = findViewById(R.id.edTextPassword)
        buttonLogin = findViewById(R.id.btn_login)


        imageViewGoToRegister?.setOnClickListener{ goToRegister() }
        buttonLogin?.setOnClickListener{login()}
    }

    private fun login(){
        val email = editTextEmail?.text.toString()
        val password = editTextPassword?.text.toString()

        Toast.makeText(this, "El email es: $email", Toast.LENGTH_LONG).show()
    }

    private fun goToRegister(){
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }


}