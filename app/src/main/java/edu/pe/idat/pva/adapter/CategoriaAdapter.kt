package edu.pe.idat.pva.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.pe.idat.pva.activities.products.ProductsActivity
import edu.pe.idat.pva.databinding.CardviewCategoriesBinding
import edu.pe.idat.pva.models.SubCategoriaResponse


class CategoriaAdapter(private var categorias: ArrayList<SubCategoriaResponse>): RecyclerView.Adapter<CategoriaAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CardviewCategoriesBinding
            .inflate(LayoutInflater.from(parent.context),parent,false)

        return CategoryViewHolder(binding)
    }

    override fun getItemCount()= categorias.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        with(holder){
            with(categorias[position]){
                binding.tvCategory.text = nombre
                Glide.with(holder.itemView.context)
                    .load(urlFoto)
                    .centerCrop()
                    .into(binding.ivCategory)
                itemView.setOnClickListener{goToProducts(this, holder.itemView.context)}
            }
        }
    }

    private fun goToProducts(category: SubCategoriaResponse, context: Context){
        val i = Intent(context, ProductsActivity::class.java)
        i.putExtra("idSubcategoria", category.idSubcategoria)
        context.startActivity(i)
    }

    inner class CategoryViewHolder (val binding: CardviewCategoriesBinding): RecyclerView.ViewHolder(binding.root)
}