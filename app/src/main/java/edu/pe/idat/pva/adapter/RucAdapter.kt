package edu.pe.idat.pva.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.pe.idat.pva.databinding.CardviewRucBinding
import edu.pe.idat.pva.models.response.RucResponse

class RucAdapter(private var rucs: ArrayList<RucResponse>,
                 private val listener: IRucAdapter): RecyclerView.Adapter<RucAdapter.ViewHolder>() {

    inner class ViewHolder (val binding: CardviewRucBinding): RecyclerView.ViewHolder(binding.root)

    interface IRucAdapter {
        fun goToRucDetail(rucResponse: RucResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardviewRucBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(rucs[position]){
                binding.tvIdRuc.text = idRuc.toString()
                binding.tvNumRuc.text = numeroRuc
                binding.llbotondetalleruc.setOnClickListener{ goDetalleRuc(this) }
            }
        }
    }

    private fun goDetalleRuc(rucResponse: RucResponse) {
        listener.goToRucDetail(rucResponse)
    }

    override fun getItemCount() = rucs.size
}