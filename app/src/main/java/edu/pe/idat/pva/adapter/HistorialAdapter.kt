package edu.pe.idat.pva.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.pe.idat.pva.databinding.CardviewOrdenBinding
import edu.pe.idat.pva.models.OrdenResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistorialAdapter(private var ordenes: ArrayList<OrdenResponse>): RecyclerView.Adapter<HistorialAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: CardviewOrdenBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardviewOrdenBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(ordenes[position]){
                binding.tvIdOrdenCv.text = this.idOrden
                binding.tvDireccionCv.text = this.direccion
                val df1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val fc = LocalDateTime.parse(this.fecha, df1)
                val fe = fc.plusDays(10)
                val df2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                binding.tvFechaCompraCv.text = fc.format(df2)
                binding.tvFechaEstimadaCv.text = fe.format(df2)
                binding.tvProductosCv.text = this.ordendetalle.size.toString()
            }
        }
    }

    override fun getItemCount() = ordenes.size
}