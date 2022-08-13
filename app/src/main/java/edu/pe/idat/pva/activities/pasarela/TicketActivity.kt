package edu.pe.idat.pva.activities.pasarela

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.pe.idat.pva.activities.HomeActivity
import edu.pe.idat.pva.databinding.ActivityTicketBinding
import edu.pe.idat.pva.db.entity.TokenEntity
import edu.pe.idat.pva.models.OrdenResponse
import edu.pe.idat.pva.providers.OrdenProvider
import edu.pe.idat.pva.providers.TokenRoomProvider

class TicketActivity : AppCompatActivity(){

    private lateinit var binding: ActivityTicketBinding
    private lateinit var ordenProvider: OrdenProvider
    private lateinit var tokenRoomProvider: TokenRoomProvider

    private lateinit var token: TokenEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llmensaje.visibility = View.GONE
        binding.llticket.visibility = View.GONE
        binding.llRucTicket.visibility = View.GONE

        binding.btnFinPasarela.setOnClickListener{gotoHome()}
        ordenProvider = ViewModelProvider(this)[OrdenProvider::class.java]
        tokenRoomProvider = ViewModelProvider(this)[TokenRoomProvider::class.java]

        getTokenFromDB()
    }

    private fun cargarDatos() {
        ordenProvider.getOrden(intent.getStringExtra("ordenId").toString(),
                                "Bearer ${token.token}").observe(this){
                                    obtenerOrden(it!!)
        }
    }

    private fun obtenerOrden(ordenResponse: OrdenResponse) {
        if (ordenResponse.tipoFop == 2) {
            binding.tvTicketTipo.text = "Factura"
            binding.llRucTicket.visibility = View.VISIBLE
            binding.tvRucTicket.text = ordenResponse.formaPago.takeLast(11)
        } else {
            binding.tvTicketTipo.text = "Boleta"
        }

        binding.tvTicketMetodo.text = "Tarjeta: ****${ordenResponse.formaPago.take(16).takeLast(4)}"
        binding.tvTicketFecha.text = ordenResponse.fecha

        val sb = StringBuilder()

        ordenResponse.ordendetalle.forEach {
            sb.append("${it.producto.nombre} x ${it.cantidad} ..... S/${String.format("%.2f",it.precio * it.cantidad)}\n")
        }

        binding.tvProductoslista.text = sb.toString()

        binding.tvTicketMonto.text = "S/${String.format("%.2f",ordenResponse.monto)}"
        binding.tvTicketIGV.text = "S/${String.format("%.2f",ordenResponse.igv)}"
        binding.tvTicketTotal.text = "S/${String.format("%.2f",ordenResponse.total)}"

        binding.progressbarTicket.visibility = View.GONE
        binding.llmensaje.visibility = View.VISIBLE
        binding.llticket.visibility = View.VISIBLE
    }

    private fun gotoHome(){
        val i = Intent(this, HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getTokenFromDB(){
        tokenRoomProvider.obtener().observe(this){
            token = it
            cargarDatos()
        }
    }

    override fun onBackPressed() {
        gotoHome()
    }
}