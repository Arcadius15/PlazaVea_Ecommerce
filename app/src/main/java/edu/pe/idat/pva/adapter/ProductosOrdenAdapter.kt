package edu.pe.idat.pva.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.pe.idat.pva.databinding.CardviewDetalleordenBinding
import edu.pe.idat.pva.models.response.OrdenDetalleResponse

class ProductosOrdenAdapter(private var productos: ArrayList<OrdenDetalleResponse>): RecyclerView.Adapter<ProductosOrdenAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: CardviewDetalleordenBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardviewDetalleordenBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(productos[position]){
                binding.tvNombreProducto.text = producto.nombre
                binding.tvPrecioUnidad.text = "S/${String.format("%.2f",precio)}"
                binding.tvCantidad.text = cantidad.toString()
                binding.tvPrecioTotal.text = "S/${String.format("%.2f",precio * cantidad)}"
                Glide.with(holder.itemView.context)
                    .load(producto.imagenUrl)
                    .into(binding.ivImagenProducto)
            }
        }
    }

    override fun getItemCount() = productos.size

}