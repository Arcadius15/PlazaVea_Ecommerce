package edu.pe.idat.pva.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import edu.pe.idat.pva.adapter.CategoriaAdapter
import edu.pe.idat.pva.databinding.FragmentHomeBinding
import edu.pe.idat.pva.providers.CategoriaProvider


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoriaProvider: CategoriaProvider


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        categoriaProvider = ViewModelProvider(requireActivity())[CategoriaProvider::class.java]

        // Inflate the layout for this fragment
        binding.rvCategorias.setHasFixedSize(true)
        binding.rvCategorias.layoutManager = GridLayoutManager(requireContext(),2)

        getCagories()

        return binding.root
    }

    private fun getCagories(){
        categoriaProvider.getAll().observe(viewLifecycleOwner){
            binding.rvCategorias.adapter = CategoriaAdapter(it)
            binding.progressbar.visibility = View.GONE
        }
    }

}