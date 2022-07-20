package edu.pe.idat.pva.activities.pasarela

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.HomeActivity
import edu.pe.idat.pva.databinding.ActivityTicketBinding
import edu.pe.idat.pva.databinding.CardviewShoppingbagBinding
import edu.pe.idat.pva.models.LoginResponse
import edu.pe.idat.pva.models.Producto
import edu.pe.idat.pva.models.UsuarioResponse
import edu.pe.idat.pva.providers.OrdenProvider
import edu.pe.idat.pva.utils.SharedPref

class TicketActivity : AppCompatActivity(){

    private lateinit var binding: ActivityTicketBinding
    private lateinit var sharedPref: SharedPref
    private lateinit var ordenProvider: OrdenProvider

    var gson = Gson()

    var monto = 0.0
    var igv = 0.0
    var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)
        binding.btnFinalizar.setOnClickListener{gotoHome()}
        //ordenProvider = ViewModelProvider(this)[OrdenProvider::class.java]
        //getUserFromSession()
        //getTokenFromSession()

    }



    private fun gotoHome(){
        val i = Intent(this, HomeActivity::class.java)
        startActivity(i)
    }




}