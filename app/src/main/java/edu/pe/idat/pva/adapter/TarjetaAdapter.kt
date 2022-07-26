package edu.pe.idat.pva.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.pe.idat.pva.databinding.CardviewTarjetaBinding
import edu.pe.idat.pva.models.TarjetaResponse

class TarjetaAdapter(private var tarjetas: ArrayList<TarjetaResponse>): RecyclerView.Adapter<TarjetaAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: CardviewTarjetaBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardviewTarjetaBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(tarjetas[position]){
                binding.tvNumTarjeta.text = "****${this.numeroTarjeta.takeLast(4)}"
                when (this.tipo){
                    1 -> binding.tvTipoTarjeta.text = "Débito"
                    2 -> binding.tvTipoTarjeta.text = "Crédito"
                }
                binding.tvCaducidad.text = this.fechaCaducidad
                binding.tvNombreEnTarjeta.text = this.nombrePropietario
            }
        }
    }

    override fun getItemCount() = tarjetas.size

}