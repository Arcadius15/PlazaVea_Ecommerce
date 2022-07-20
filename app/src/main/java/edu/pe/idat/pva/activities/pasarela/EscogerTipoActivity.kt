package edu.pe.idat.pva.activities.pasarela

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.DireccionRegistroActivity
import edu.pe.idat.pva.activities.RegRucActivity
import edu.pe.idat.pva.databinding.ActivityEscogerTipoBinding

class EscogerTipoActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityEscogerTipoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEscogerTipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnelegir.setOnClickListener(this)
        binding.btnGoBackCarrito.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.btnelegir -> elegirTipo()
            R.id.btnGoBackCarrito -> finish()
        }
    }

    private fun elegirTipo() {
        if (binding.rbBoleta.isChecked) {
            var i = Intent(this, DireccionRegistroActivity::class.java)
            i.putExtra("tipo","Boleta")
            startActivity(i)
        } else if (binding.rbFactura.isChecked) {
            var i = Intent(this, RegRucActivity::class.java)
            i.putExtra("tipo","Factura")
            startActivity(i)
        } else {
            Toast.makeText(
                applicationContext,
                "Por favor, escoga un tipo.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}