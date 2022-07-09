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
import edu.pe.idat.pva.models.Categoria
import edu.pe.idat.pva.models.Product
import edu.pe.idat.pva.utils.SharedPref


class ProductsAdapter(val context: Activity, val productos: ArrayList<Product>): RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

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
        holder.textViewName.text = product.name
        holder.textViewPrice.text = "S/${product.price}"
        Glide.with(context).load(product.image1).into(holder.imageViewProduct)

        holder.itemView.setOnClickListener{goToDetail(product)}
    }

    private fun goToDetail(product: Product){

        val i = Intent(context, ProductsDetailActivity::class.java)
        i.putExtra("product", product.toJson())
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