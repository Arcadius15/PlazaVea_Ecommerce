package edu.pe.idat.pva.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnGoRegister.setOnClickListener{ goToRegister() }
        binding.btnLogin.setOnClickListener{gotoHome()}
    }

    private fun goToRegister(){
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }


    private fun gotoHome(){
        val i = Intent(this, HomeActivity::class.java)
        startActivity(i)
    }


}