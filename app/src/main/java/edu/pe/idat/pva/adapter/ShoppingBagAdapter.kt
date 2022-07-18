package edu.pe.idat.pva.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
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

    val TAG = "ShoppingAdapter"
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
                binding.tvContador.text = "${quantity}"
                binding.tvPrecio.text = "S/${String.format("%.2f",precioRegular * quantity!!)}"
                Glide.with(holder.itemView.context)
                    .load(imagenUrl)
                    .into(binding.ivProductBag)
                binding.ivAdd.setOnClickListener{addItem(this, holder)}
                binding.ivRemove.setOnClickListener{removeItem(this, holder)}
                binding.ivDelete.setOnClickListener{deleteItem(this)}
                holder.itemView.setOnClickListener{goToDetail(this)}
            }

        }
    }

    private fun deleteItem(producto: Producto) {
        productos.remove(producto)
        sharedPref.save("shopBag", productos)
        notifyDataSetChanged()
    }

    private fun getIndexOf(idProduct: String): Int{
        var ps = 0
        for(p in productos){
            if(p.idProducto == idProduct){
                return ps
            }
            ps++
        }

        return -1
    }



    private fun addItem(product: Producto, holder: ShoppingBagViewHolder){

        val index = getIndexOf(product.idProducto)
        product.quantity = product.quantity!! + 1
        productos[index].quantity = product.quantity

        with(holder){
            binding.tvContador.text = "${product.quantity}"
            binding.tvPrecio.text = "S/${String.format("%.2f",product.quantity!! * product.precioRegular)}"
            if(product.quantity == 10){
                binding.ivAdd.isEnabled = false
            }
        }

        sharedPref.save("shopBag", productos)

        notifyDataSetChanged()
    }


    private fun removeItem(product: Producto, holder: ShoppingBagViewHolder){
       if(product.quantity!! > 1)
       {
           val index = getIndexOf(product.idProducto)
           product.quantity = product.quantity!! - 1
           productos[index].quantity = product.quantity

           with(holder){
               binding.tvContador.text = "${product.quantity}"
               binding.tvPrecio.text = "S/${String.format("%.2f",product.quantity!! * product.precioRegular)}"
               if (!binding.ivAdd.isEnabled){
                   binding.ivAdd.isEnabled = true
               }
           }

           sharedPref.save("shopBag", productos)

           notifyDataSetChanged()
       }
    }

    private fun goToDetail(product: Producto){

        val i = Intent(context, ProductsDetailActivity::class.java)
        i.putExtra("producto", product.toJson())
        context.startActivity(i)
    }



    inner class  ShoppingBagViewHolder(val binding: CardviewShoppingbagBinding): RecyclerView.ViewHolder(binding.root)

}
