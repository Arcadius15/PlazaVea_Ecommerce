package edu.pe.idat.pva.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.pe.idat.pva.databinding.FragmentProductoBinding
import edu.pe.idat.pva.databinding.ItemCategoriaBinding
import edu.pe.idat.pva.models.SubCategoriaItem

class CategoriaAdapter: RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {
    inner class CategoriaViewHolder(val binding: ItemCategoriaBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<SubCategoriaItem>(){
        override fun areItemsTheSame(
            oldItem: SubCategoriaItem,
            newItem: SubCategoriaItem
        ): Boolean {
            return  oldItem.idSubcategoria == newItem.idSubcategoria
        }

        override fun areContentsTheSame(
            oldItem: SubCategoriaItem,
            newItem: SubCategoriaItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var sub: List<SubCategoriaItem>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount() = sub.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        return CategoriaViewHolder(ItemCategoriaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        holder.binding.apply {
            val subcat = sub[position]
            tvTitle.text =  subcat.nombre
        }
    }
}