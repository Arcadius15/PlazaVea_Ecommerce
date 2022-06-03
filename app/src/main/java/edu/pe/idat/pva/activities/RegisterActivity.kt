package edu.pe.idat.pva.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() , View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageviewGoToLogin.setOnClickListener{toast()}
        binding.btnRegistrar.setOnClickListener(this)
    }

    override fun onClick(p0: View) {


    }

    private fun goToLogin(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun toast(){
        Toast.makeText(this, "hizo click", Toast.LENGTH_LONG).show()
    }



}