package edu.pe.idat.pva.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.products.detail.ProductsDetailActivity
import edu.pe.idat.pva.models.Product
import edu.pe.idat.pva.models.Producto
import edu.pe.idat.pva.utils.SharedPref


class ProductsAdapter(val context: Activity, val productos: ArrayList<Producto>): RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    val sharedPref = SharedPref(context)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_product, parent, false)
       return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  productos.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productos[position]
        holder.textViewName.text = product.nombre
        holder.textViewPrice.text = "S/${product.precioRegular}"
        Glide.with(context).load(product.imagenUrl).into(holder.imageViewProduct)

        holder.itemView.setOnClickListener{goToDetail(product)}
    }

    private fun goToDetail(product: Producto){

        val i = Intent(context, ProductsDetailActivity::class.java)
        .putExtra("idProducto", product.toJson())
        context.startActivity(i)
    }



    class  ProductViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewName: TextView
        val textViewPrice: TextView
        val imageViewProduct: ImageView

        init {
            textViewName = view.findViewById(R.id.tv_name)
            textViewPrice = view.findViewById(R.id.tv_productPrice)
            imageViewProduct = view.findViewById(R.id.iv_product)
        }
    }
}