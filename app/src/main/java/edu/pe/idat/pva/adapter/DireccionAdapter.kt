package edu.pe.idat.pva.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.pe.idat.pva.databinding.CardviewDireccionBinding
import edu.pe.idat.pva.models.response.DireccionResponse

class DireccionAdapter(private var direcciones: ArrayList<DireccionResponse>,
                       private val listener: IDireccionAdapter): RecyclerView.Adapter<DireccionAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: CardviewDireccionBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardviewDireccionBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(direcciones[position]){
                binding.tvIdDireccion.text = idDireccion.toString()
                binding.tvDireccionDetalle.text = direccion
                binding.llVerMapa.setOnClickListener{ verEnMapa(this) }
            }
        }
    }

    private fun verEnMapa(direccionResponse: DireccionResponse) {
        listener.goToMap(direccionResponse)
    }

    override fun getItemCount() = direcciones.size

    interface IDireccionAdapter{
        fun goToMap(direccionResponse: DireccionResponse)
    }
}