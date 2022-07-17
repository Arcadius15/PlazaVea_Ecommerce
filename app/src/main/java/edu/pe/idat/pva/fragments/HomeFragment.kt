package edu.pe.idat.pva.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.pe.idat.pva.R
import edu.pe.idat.pva.adapter.CategoriaAdapter
import edu.pe.idat.pva.databinding.FragmentHomeBinding
import edu.pe.idat.pva.models.SubCategoriaResponse
import edu.pe.idat.pva.providers.CategoriaProvider
import edu.pe.idat.pva.utils.SharedPref


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoriaProvider: CategoriaProvider

    var adapter: CategoriaAdapter? = null
    var sharedPref: SharedPref? = null

    var categories = ArrayList<SubCategoriaResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        categoriaProvider = ViewModelProvider(requireActivity())[CategoriaProvider::class.java]

        // Inflate the layout for this fragment
        binding.rvCategorias.setHasFixedSize(true)
        binding.rvCategorias.layoutManager = GridLayoutManager(requireContext(),2)

        sharedPref = SharedPref(requireActivity())

        getCagories()

        return binding.root
    }

    private fun getCagories(){
        categoriaProvider.getAll().observe(viewLifecycleOwner){
            binding.rvCategorias.adapter = CategoriaAdapter(it)
        }
    }


}