package edu.pe.idat.pva.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.pe.idat.pva.R
import edu.pe.idat.pva.activities.products.ProductsActivity
import edu.pe.idat.pva.models.SubCategoriaResponse
import edu.pe.idat.pva.utils.SharedPref


class CategoriaAdapter(val context: Activity, val categorias: ArrayList<SubCategoriaResponse>): RecyclerView.Adapter<CategoriaAdapter.CategoryViewHolder>() {

    val sharedPref = SharedPref(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_categories, parent, false)
       return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  categorias.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categorias[position] //cada rol
        holder.textViewCategory.text = category.nombre
        Glide.with(context).load(category.urlFoto).into(holder.imageViewCategory)

        holder.itemView.setOnClickListener{goToProducts(category)}
    }

    private fun goToProducts(category: SubCategoriaResponse){
        val i = Intent(context, ProductsActivity::class.java)
        i.putExtra("idSubcategoria", category.idSubcategoria)
        context.startActivity(i)

    }

    class  CategoryViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textViewCategory: TextView
        val imageViewCategory: ImageView

        init {
            textViewCategory = view.findViewById(R.id.tv_category)
            imageViewCategory = view.findViewById(R.id.iv_category)
        }
    }
}