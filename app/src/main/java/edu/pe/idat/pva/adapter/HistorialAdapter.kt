package edu.pe.idat.pva.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.pe.idat.pva.databinding.CardviewOrdenBinding
import edu.pe.idat.pva.models.OrdenResponse
import edu.pe.idat.pva.providers.ProductsProvider
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class HistorialAdapter(private var ordenes: ArrayList<OrdenResponse>): RecyclerView.Adapter<HistorialAdapter.ViewHolder>() {

    private lateinit var productoProvider: ProductsProvider

    private lateinit var lco: LifecycleOwner

    inner class ViewHolder (val binding: CardviewOrdenBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardviewOrdenBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        productoProvider = ViewModelProvider(parent.findViewTreeViewModelStoreOwner()!!)[ProductsProvider::class.java]

        lco = parent.lifecycleOwner!!

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
                fc.time = df1.parse(fecha)
                val df2 = SimpleDateFormat("yyyy-MM-dd")
                df2.timeZone = TimeZone.getTimeZone("America/Lima")
                binding.tvFechaCompraCv.text = df2.format(fc.time)
                fc.add(Calendar.DATE, 10)
                binding.tvFechaEstimadaCv.text = df2.format(fc.time)
                binding.tvProductosCv.text = ordendetalle.size.toString()
                println(ordendetalle[0].producto.idProducto)
                val producto = productoProvider.findById(ordendetalle[0].producto.idProducto)//.observe(lco){
//                    println(it.nombre)
//                    Glide.with(holder.itemView.context)
//                        .load(it.imagenUrl)
//                        .into(binding.ivPrimerProducto)
                //}
            }
        }
    }

    override fun getItemCount() = ordenes.size

    val View.lifecycleOwner get() = ViewTreeLifecycleOwner.get(this)
}