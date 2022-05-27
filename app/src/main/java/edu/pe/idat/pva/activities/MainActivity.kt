package edu.pe.idat.pva.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var imageViewGoToRegister: ImageView? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageViewGoToRegister = findViewById(R.id.imageview_go_register)

        imageViewGoToRegister?.setOnClickListener{ goToRegister() }
    }

    private fun goToRegister(){
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }
}