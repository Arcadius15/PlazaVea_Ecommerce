package edu.pe.idat.pva.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.pe.idat.pva.databinding.CardviewTarjetaBinding
import edu.pe.idat.pva.models.response.TarjetaResponse

class TarjetaAdapter(private var tarjetas: ArrayList<TarjetaResponse>,
                     private val listener: ITarjetaAdapter): RecyclerView.Adapter<TarjetaAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: CardviewTarjetaBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardviewTarjetaBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(tarjetas[position]){
                binding.tvNumTarjeta.text = "****${numeroTarjeta.takeLast(4)}"
                when (tipo){
                    1 -> binding.tvTipoTarjeta.text = "Débito"
                    2 -> binding.tvTipoTarjeta.text = "Crédito"
                }
                binding.tvCaducidad.text = fechaCaducidad.substring(5,7) + "/" + fechaCaducidad.substring(2,4)
                binding.tvNombreEnTarjeta.text = nombrePropietario
                binding.llbotondetalletarjeta.setOnClickListener{ verDetalles(this) }
            }
        }
    }

    private fun verDetalles(tarjetaResponse: TarjetaResponse) {
        listener.goToTarjetaDet(tarjetaResponse)
    }

    override fun getItemCount() = tarjetas.size

    interface ITarjetaAdapter{
        fun goToTarjetaDet(tarjetaResponse: TarjetaResponse)
    }
}