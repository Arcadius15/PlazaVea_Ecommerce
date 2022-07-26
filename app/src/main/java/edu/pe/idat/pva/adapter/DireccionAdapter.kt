package edu.pe.idat.pva.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.pe.idat.pva.databinding.CardviewDireccionBinding
import edu.pe.idat.pva.models.DireccionResponse

class DireccionAdapter(private var direcciones: ArrayList<DireccionResponse>): RecyclerView.Adapter<DireccionAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: CardviewDireccionBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardviewDireccionBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(direcciones[position]){
                binding.tvIdDireccion.text = this.idDireccion.toString()
                binding.tvDireccionDetalle.text = this.direccion
                binding.llVerMapa.setOnClickListener{ verEnMapa(this.latitud,this.longitud) }
            }
        }
    }

    private fun verEnMapa(latitud: Double, longitud: Double) {

    }

    override fun getItemCount() = direcciones.size

}