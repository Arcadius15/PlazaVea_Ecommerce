package edu.pe.idat.pva.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.products.ProductsActivity
import edu.pe.idat.pva.activities.products.detail.ProductsDetailActivity
import edu.pe.idat.pva.databinding.CardviewShoppingbagBinding
import edu.pe.idat.pva.models.Product
import edu.pe.idat.pva.models.Producto
import edu.pe.idat.pva.utils.SharedPref

class ShoppingBagAdapter(val context: Activity, private var productos: ArrayList<Producto>): RecyclerView.Adapter<ShoppingBagAdapter.ShoppingBagViewHolder>() {

    val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingBagViewHolder {
        val binding = CardviewShoppingbagBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ShoppingBagViewHolder(binding)
    }

    override fun getItemCount() = productos.size


    override fun onBindViewHolder(holder: ShoppingBagViewHolder, position: Int) {
        with(holder){
            with(productos[position]){
                binding.tvNameBag.text = nombre
                binding.tvPrice.text = "S/${precioRegular}"
                Glide.with(holder.itemView.context)
                    .load(imagenUrl)
                    .into(binding.ivProductBag)
            }
        }

        // holder.itemView.setOnClickListener{goToDetail(product)}
    }

    private fun goToDetail(product: Product){

        val i = Intent(context, ProductsDetailActivity::class.java)
        // i.putExtra("product", product.toJson())
        context.startActivity(i)
    }



    inner class  ShoppingBagViewHolder(val binding: CardviewShoppingbagBinding): RecyclerView.ViewHolder(binding.root)

}
