package edu.pe.idat.pva.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.pe.idat.pva.databinding.CardviewOrdenBinding
import edu.pe.idat.pva.models.OrdenResponse
import java.text.SimpleDateFormat
import java.util.*

class HistorialAdapter(private var ordenes: ArrayList<OrdenResponse>,
                       private var listener: IHistorialAdapter): RecyclerView.Adapter<HistorialAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: CardviewOrdenBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardviewOrdenBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(ordenes[position]){
                binding.tvIdOrdenCv.text = idOrden
                binding.tvDireccionCv.text = direccion
                val fc = Calendar.getInstance()
                val df1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                df1.timeZone = TimeZone.getTimeZone("America/Lima")
                fc.time = df1.parse(fecha) as Date
                val df2 = SimpleDateFormat("yyyy-MM-dd")
                df2.timeZone = TimeZone.getTimeZone("America/Lima")
                binding.tvFechaCompraCv.text = df2.format(fc.time)
                fc.add(Calendar.DATE, 10)
                binding.tvFechaEstimadaCv.text = df2.format(fc.time)
                binding.tvProductosCv.text = ordendetalle.size.toString()
                Glide.with(holder.itemView.context)
                    .load(ordendetalle[0].producto.imagenUrl)
                    .into(binding.ivPrimerProducto)
                holder.itemView.setOnClickListener{listener.goDetail(this)}
            }
        }
    }

    override fun getItemCount() = ordenes.size

    interface IHistorialAdapter {
        fun goDetail(ordenResponse: OrdenResponse)
    }
}