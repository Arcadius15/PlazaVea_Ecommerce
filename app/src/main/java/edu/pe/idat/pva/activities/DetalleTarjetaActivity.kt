package edu.pe.idat.pva.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityDetalleTarjetaBinding
import edu.pe.idat.pva.providers.TarjetaProvider
import edu.pe.idat.pva.utils.SharedPref

class DetalleTarjetaActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPref

    private lateinit var binding: ActivityDetalleTarjetaBinding
    private lateinit var tarjetaProvider: TarjetaProvider

    var tipo= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleTarjetaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = SharedPref(this)

        tarjetaProvider = ViewModelProvider(this)[TarjetaProvider::class.java]
    }
}