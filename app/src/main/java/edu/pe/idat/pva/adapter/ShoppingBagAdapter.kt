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
import edu.pe.idat.pva.models.Product
import edu.pe.idat.pva.models.Producto
import edu.pe.idat.pva.utils.SharedPref


class ShoppingBagAdapter(val context: Activity, val productos: ArrayList<Producto>): RecyclerView.Adapter<ShoppingBagAdapter.ShoppingBagViewHolder>() {

    val sharedPref = SharedPref(context)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingBagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_shoppingbag, parent, false)
       return ShoppingBagViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  productos.size
    }

    override fun onBindViewHolder(holder: ShoppingBagViewHolder, position: Int) {
        val product = productos[position]
        holder.textViewName.text = product.nombre
        holder.textViewPrice.text = "S/${product.precioRegular}"
        Glide.with(context).load(product.imagenUrl).into(holder.imageViewProduct)

       // holder.itemView.setOnClickListener{goToDetail(product)}
    }

    private fun goToDetail(product: Product){

        val i = Intent(context, ProductsDetailActivity::class.java)
       // i.putExtra("product", product.toJson())
        context.startActivity(i)
    }



    class  ShoppingBagViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewName: TextView
        val textViewPrice: TextView
        val textViewContador: TextView
        val imageViewProduct: ImageView
        val imageViewAdd: ImageView
        val imageViewRemove: ImageView
        val imageViewDelete: ImageView


        init {
            textViewName = view.findViewById(R.id.tv_nameBag)
            textViewPrice = view.findViewById(R.id.tv_price)
            textViewContador = view.findViewById(R.id.tv_contador)
            imageViewProduct = view.findViewById(R.id.iv_productBag)
            imageViewAdd = view.findViewById(R.id.iv_add)
            imageViewRemove = view.findViewById(R.id.iv_remove)
            imageViewDelete = view.findViewById(R.id.iv_delete)
        }
    }
}