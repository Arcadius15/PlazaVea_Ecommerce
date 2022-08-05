package edu.pe.idat.pva.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.pe.idat.pva.R
import edu.pe.idat.pva.databinding.ActivityDetalleOrdenBinding
import edu.pe.idat.pva.models.OrdenResponse

class DetalleOrdenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleOrdenBinding

    private lateinit var ordenResponse: OrdenResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleOrdenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordenResponse = intent.getSerializableExtra("orden") as OrdenResponse
    }
}